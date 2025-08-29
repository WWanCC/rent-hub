package renthub.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import renthub.GraphQL.EntityAlias;
import renthub.GraphQL.PaginationInput;
import renthub.GraphQL.SortInput;
import renthub.auth.strategy.QueryAuthStrategy;
import renthub.domain.po.House;
import renthub.domain.po.Region;
import renthub.domain.po.RentalContract;
import renthub.domain.po.User;
import renthub.utils.SortUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CommonQueryService {

    private final ApplicationContext applicationContext;
    private final SortUtil sortUtil;

    /**
     * 【策略中心】
     * 存储所有查询鉴权策略的 Map。
     * Key: 实体类的 Class 对象
     * Value: 对应的鉴权策略实例
     */
    private final Map<Class<?>, QueryAuthStrategy<?>> authStrategyMap;

    /**
     * 【白名单】
     * 将 GraphQL Schema 中定义的 EntityAlias 枚举，与后端的实体类 Class 进行安全映射。
     * 这是防止前端请求任意表的“第一道防线”。
     */
    private static final Map<EntityAlias, Class<?>> ENTITY_ALIAS_MAP = Map.of(
            EntityAlias.HOUSES, House.class,
            EntityAlias.CONTRACTS, RentalContract.class,
            EntityAlias.USERS, User.class,
            EntityAlias.REGIONS, Region.class
            // 未来想开放新表的查询，只需在此处注册即可
    );

    /**
     * 构造函数，由 Spring 自动注入所有需要的组件。
     *
     * @param applicationContext Spring 的应用上下文，用于动态获取 Bean。
     * @param sortUtil           我们封装的通用排序工具。
     * @param strategies         Spring 会自动找到所有实现了 QueryAuthStrategy 接口的 Bean，并注入一个 List。
     */
    public CommonQueryService(ApplicationContext applicationContext, SortUtil sortUtil, List<QueryAuthStrategy<?>> strategies) {
        this.applicationContext = applicationContext;
        this.sortUtil = sortUtil;

        // 初始化策略中心：将所有鉴权策略实例存入 Map，方便后续快速查找
        this.authStrategyMap = new ConcurrentHashMap<>();
        strategies.forEach(strategy -> authStrategyMap.put(strategy.getEntityClass(), strategy));
    }

    /**
     * 通用查询的核心方法，被 GraphQL Controller 调用。
     *
     * @param alias      前端传入的实体别名枚举
     * @param filter     前端传入的、结构任意的过滤条件 Map (来自 JSON 标量)
     * @param sort       前端传入的排序条件列表
     * @param pagination 【可选的】分页参数对象。如果为null，则执行不分页的全量查询。
     * @return 分页后的查询结果
     */
    public <T> IPage<T> query(EntityAlias alias, Map<String, Object> filter, List<SortInput> sort, PaginationInput pagination) {
        // --- 1. 实体类型解析与校验 ---
        Class<T> entityClass = (Class<T>) ENTITY_ALIAS_MAP.get(alias);
        if (entityClass == null) {
            throw new IllegalArgumentException("不支持的实体别名查询：" + alias);
        }

        // --- 2. 动态获取所需组件 ---
        BaseMapper<T> mapper = getMapperByEntityClass(entityClass);
        Set<String> validColumns = sortUtil.getValidSortColumns(entityClass);

        // --- 3. 初始化查询构造器 ---
        QueryWrapper<T> wrapper = new QueryWrapper<>();

        // --- 4. 【核心安全步骤】应用动态鉴权策略 ---
        QueryAuthStrategy<T> strategy = (QueryAuthStrategy<T>) authStrategyMap.get(entityClass);
        if (strategy != null) {
            strategy.applyAuth(wrapper);
        } else {
            throw new SecurityException("安全策略缺失：没有为 " + entityClass.getSimpleName() + " 配置查询权限");
        }

        // --- 5. 【动态构建 WHERE 子句 (增强版)】 ---
        buildWhereClause(wrapper, filter, validColumns);

        // --- 6. 【复用 SortUtil 处理 ORDER BY 子句 (优化版)】 ---
        if (sort != null && !sort.isEmpty()) {
            SortInput firstSort = sort.get(0);
            sortUtil.applySort(wrapper, entityClass, firstSort.getField(), firstSort.getDirection().name());
        } else {
            sortUtil.applySort(wrapper, entityClass, null, null);
        }

        // --- 7. 【核心改动】根据 pagination 参数是否存在，决定查询模式 ---
        if (pagination != null) {
            // **模式一：执行正常的分页查询**
            IPage<T> pageRequest = new Page<>(pagination.getPage(), pagination.getSize());
            return mapper.selectPage(pageRequest, wrapper);
        } else {
            // **模式二：执行不分页的全量查询**
            List<T> allRecords = mapper.selectList(wrapper);

            // 为了保持返回类型(IPage)的一致性，手动将 List 包装成一个 IPage 对象
            IPage<T> pageResult = new Page<>();
            pageResult.setRecords(allRecords);
            pageResult.setTotal(allRecords.size());
            pageResult.setCurrent(1);
            pageResult.setPages(1);
            pageResult.setSize(allRecords.size());
            return pageResult;
        }
    }

    /**
     * 【新增】一个私有的、独立的辅助方法，专门用于构建 WHERE 子句。
     * 实现了对 _gte, _lte, _like 等操作符的支持。
     *
     * @param wrapper      需要构建的 QueryWrapper
     * @param filter       前端传入的过滤条件 Map
     * @param validColumns 该表的字段白名单
     * @param <T>          实体泛型
     */
    private <T> void buildWhereClause(QueryWrapper<T> wrapper, Map<String, Object> filter, Set<String> validColumns) {
        if (filter == null || filter.isEmpty()) {
            return;
        }
        filter.forEach((key, value) -> {
            String fieldName = key;
            String operator = "eq";
            int lastUnderscoreIndex = key.lastIndexOf('_');
            if (lastUnderscoreIndex > 0) {
                String potentialOperator = key.substring(lastUnderscoreIndex + 1);
                if (Set.of("eq", "gte", "lte", "like").contains(potentialOperator)) {
                    fieldName = key.substring(0, lastUnderscoreIndex);
                    operator = potentialOperator;
                }
            }
            String column = StringUtils.camelToUnderline(fieldName);
            if (validColumns.contains(column)) {
                switch (operator) {
                    case "gte": wrapper.ge(column, value); break;
                    case "lte": wrapper.le(column, value); break;
                    case "like": wrapper.like(column, value); break;
                    default: wrapper.eq(column, value); break;
                }
            }
        });
    }

    /**
     * 根据实体类的 Class，动态地从 Spring 容器中获取其对应的 Mapper Bean。
     *
     * @param entityClass 实体类的 Class 对象
     * @return 对应的 Mapper 实例
     */
    private <T> BaseMapper<T> getMapperByEntityClass(Class<T> entityClass) {
        // 遵循 MyBatis-Plus 默认的 Mapper Bean 命名约定: "实体类名（首字母小写） + Mapper"
        String beanName = uncapitalize(entityClass.getSimpleName()) + "Mapper";
        try {
            return (BaseMapper<T>) applicationContext.getBean(beanName);
        } catch (Exception e) {
            throw new IllegalStateException("无法从Spring容器中找到实体 " + entityClass.getSimpleName() + " 对应的 Mapper Bean，请检查命名是否为: " + beanName, e);
        }
    }

    /**
     * 将字符串的第一个字符转换为小写。
     * 这是一个独立的、无任何外部依赖的实现，用于替代可能存在版本问题的 StringUtils.uncapitalize。
     *
     * @param str 需要转换的字符串
     * @return 转换后的字符串
     */
    private String uncapitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }
}