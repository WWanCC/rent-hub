package renthub.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
            EntityAlias.USERS, User.class
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
        Set<String> validColumns = sortUtil.getValidSortColumns(entityClass); // 获取字段白名单

        // --- 3. 初始化查询构造器 ---
        QueryWrapper<T> wrapper = new QueryWrapper<>();

        // --- 4. 【核心安全步骤】应用动态鉴权策略 ---
        QueryAuthStrategy<T> strategy = (QueryAuthStrategy<T>) authStrategyMap.get(entityClass);
        if (strategy != null) {
            // 执行该实体专属的权限过滤逻辑（如：添加 user_id = ? 条件）
            strategy.applyAuth(wrapper);
        }
        else {
            // 如果一个实体在白名单中，但没有为其配置鉴权策略，出于安全考虑，默认拒绝访问
            throw new SecurityException("安全策略缺失：没有为 " + entityClass.getSimpleName() + " 配置查询权限");
        }

        // --- 5. 【动态构建 WHERE 子句】 ---
        if (filter != null && !filter.isEmpty()) {
            filter.forEach((key, value) -> {
                // a. 将前端传入的驼峰 key (e.g., "userId") 转换为数据库下划线列名 (e.g., "user_id")
                String column = StringUtils.camelToUnderline(key);

                // b. 【安全校验】确认转换后的列名存在于该表的字段白名单中
                if (validColumns.contains(column)) {
                    // c. 只支持简单的“等于”查询，以防止复杂的SQL注入
                    //    注意：对于非字符串类型，MyBatis-Plus 会自动处理类型
                    wrapper.eq(column, value);
                }
                // 如果 key 不在白名单中，则静默忽略，不报错也不应用
            });
        }

        // --- 6. 【复用 SortUtil 处理 ORDER BY 子句】 ---
        if (sort != null && !sort.isEmpty()) {
            sort.forEach(s -> sortUtil.applySort(wrapper, entityClass, s.getField(), s.getDirection().name()));
        }
        else {
            // 如果前端未提供排序，则应用默认排序
            sortUtil.applySort(wrapper, entityClass, null, null);
        }

        // --- 7. 执行最终的分页查询 ---
        IPage<T> pageRequest = new Page<>(page, size);
        return mapper.selectPage(pageRequest, wrapper);
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

    // --- 【新增】我们自己实现的 uncapitalize 方法 ---

    /**
     * 将字符串的第一个字符转换为小写。
     * 这是一个独立的、无任何外部依赖的实现。
     *
     * @param str 需要转换的字符串
     * @return 转换后的字符串
     */
    private String uncapitalize(String str) {
        // 防御性编程：检查输入是否为空或 null
        if (str == null || str.isEmpty()) {
            return str;
        }

        // 如果只有一个字符，直接转为小写
        if (str.length() == 1) {
            return str.toLowerCase();
        }

        // 将第一个字符转为小写，然后拼接上剩余的字符串
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }
}