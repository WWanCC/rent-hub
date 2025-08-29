package renthub.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import renthub.GraphQL.EntityAlias;
import renthub.GraphQL.SortInput;
import renthub.auth.strategy.QueryAuthStrategy;
import renthub.domain.po.House;
import renthub.domain.po.RentalContract;
import renthub.domain.po.User;
import renthub.utils.SortUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
//@RequiredArgsConstructor
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

    public CommonQueryService(ApplicationContext applicationContext, SortUtil sortUtil, List<QueryAuthStrategy<?>> strategies) {
        this.applicationContext = applicationContext;
        this.sortUtil = sortUtil;

        // 手动初始化策略中心 Map
        this.authStrategyMap = new ConcurrentHashMap<>();
        // 遍历 Spring 注入的所有策略实例，并将它们按 Class 类型存入 Map
        if (strategies != null) {
            strategies.forEach(strategy -> this.authStrategyMap.put(strategy.getEntityClass(), strategy));
        }
    }

    /**
     * 【白名单】
     * 将 GraphQL Schema 中定义的 EntityAlias 枚举，与后端的实体类 Class 进行安全映射。
     * 这是防止前端请求任意表的“第一道防线”。
     */
    private static final Map<EntityAlias, Class<?>> ENTITY_ALIAS_MAP = Map.of(
            EntityAlias.HOUSES, House.class,
            EntityAlias.CONTRACTS, RentalContract.class,
            EntityAlias.USERS, User.class
            // 未来想开放新表的查询，只需在此处注册即可
    );

    // 【注】: 此处原有的构造函数已被 @RequiredArgsConstructor (Lombok) 替代，功能相同。
    // Spring会自动注入 final 字段，并将 List<QueryAuthStrategy> 注入到 authStrategyMap 中。
    // 如果不使用 Lombok，则需要手动编写构造函数来初始化 authStrategyMap。


    /**
     * 通用查询的核心方法，被 GraphQL Controller 调用。
     *
     * @param alias  前端传入的实体别名枚举
     * @param filter 前端传入的、结构任意的过滤条件 Map (来自 JSON 标量)
     * @param sort   前端传入的排序条件列表
     * @param page   页码
     * @param size   每页数量
     * @return 分页后的查询结果
     */
    public <T> IPage<T> query(EntityAlias alias, Map<String, Object> filter, List<SortInput> sort, int page, int size) {
        // --- 1. 实体类型解析与校验 ---
        Class<T> entityClass = (Class<T>) ENTITY_ALIAS_MAP.get(alias);
        if (entityClass == null) {
            // 如果前端传入一个不在白名单中的别名，立即拒绝
            throw new IllegalArgumentException("不支持的实体别名查询：" + alias);
        }

        // --- 2. 动态获取所需组件 ---
        BaseMapper<T> mapper = getMapperByEntityClass(entityClass);
        // 调用 SortUtil 的公共方法获取字段白名单，利用其缓存机制
        Set<String> validColumns = sortUtil.getValidSortColumns(entityClass);

        // --- 3. 初始化查询构造器 ---
        QueryWrapper<T> wrapper = new QueryWrapper<>();

        // --- 4. 【核心安全步骤】应用动态鉴权策略 ---
        QueryAuthStrategy<T> strategy = (QueryAuthStrategy<T>) authStrategyMap.get(entityClass);
        if (strategy != null) {
            // 执行该实体专属的权限过滤逻辑（如：添加 user_id = ? 条件）
            strategy.applyAuth(wrapper);
        } else {
            // 如果一个实体在白名单中，但没有为其配置鉴权策略，出于安全考虑，默认拒绝访问
            throw new SecurityException("安全策略缺失：没有为 " + entityClass.getSimpleName() + " 配置查询权限");
        }

        // --- 5. 【动态构建 WHERE 子句 (增强版)】 ---
        buildWhereClause(wrapper, filter, validColumns);

        // --- 6. 【复用 SortUtil 处理 ORDER BY 子句 (优化版)】 ---
        if (sort != null && !sort.isEmpty()) {
            // 如果前端提供了排序条件，我们只取【第一个】来应用
            SortInput firstSort = sort.get(0);
            sortUtil.applySort(wrapper, entityClass, firstSort.getField(), firstSort.getDirection().name());
        } else {
            // 如果前端未提供排序，则两个排序参数都传 null，让 SortUtil 启用默认排序
            sortUtil.applySort(wrapper, entityClass, null, null);
        }

        // --- 7. 执行最终的分页查询 ---
        IPage<T> pageRequest = new Page<>(page, size);
        // 【注意】: 这里返回的是 IPage<T> (实体)，而不是 IPage<Map<String, Object>>
        // GraphQL 会自动根据前端请求的字段进行“剪裁”，所以我们返回完整的实体对象即可。
        return mapper.selectPage(pageRequest, wrapper);
    }

    /**
     * 【新增】一个私有的、独立的辅助方法，专门用于构建 WHERE 子句。
     * 实现了对 _gte, _lte, _like 等操作符的支持。
     *
     * @param wrapper       需要构建的 QueryWrapper
     * @param filter        前端传入的过滤条件 Map
     * @param validColumns  该表的字段白名单
     * @param <T>           实体泛型
     */
    private <T> void buildWhereClause(QueryWrapper<T> wrapper, Map<String, Object> filter, Set<String> validColumns) {
        if (filter == null || filter.isEmpty()) {
            return;
        }

        filter.forEach((key, value) -> {
            String fieldName = key;
            String operator = "eq"; // 默认操作符为“等于”

            // 解析 key，分离出字段名和操作符 (e.g., "pricePerMonth_gte")
            int lastUnderscoreIndex = key.lastIndexOf('_');
            if (lastUnderscoreIndex > 0) {
                String potentialOperator = key.substring(lastUnderscoreIndex + 1);
                // 校验是否是合法的、我们支持的操作符
                if (Set.of("eq", "gte", "lte", "like").contains(potentialOperator)) {
                    fieldName = key.substring(0, lastUnderscoreIndex);
                    operator = potentialOperator;
                }
            }

            // 将前端传入的驼峰字段名 (e.g., "pricePerMonth") 转换为数据库下划线列名 (e.g., "price_per_month")
            String column = StringUtils.camelToUnderline(fieldName);

            // 【安全校验】确认转换后的列名存在于该表的字段白名单中
            if (validColumns.contains(column)) {
                // 根据解析出的操作符，调用 QueryWrapper 不同的方法
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
            // 如果找不到，说明项目结构有问题（比如Mapper没加@Mapper注解，或命名不规范）
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