package renthub.GraphQL;

/**
 * 与 GraphQL Schema 中定义的 "EntityAlias" 枚举完全对应。
 * <p>
 * 这个枚举作为通用查询的“白名单”，定义了所有允许前端通过
 * /api/common/query 或 GraphQL 的 queryEntities 接口查询的实体别名。
 */
public enum EntityAlias {

    // 枚举成员的名字，必须和 schema.graphqls 中的定义【完全一致】
    HOUSES,

    CONTRACTS,

    USERS,
    REGIONS,
    EMP,
    TAG,
    NOTIFICATIONS

    // 未来如果想开放对新表的查询，比如 "Regions"，
    // 只需要在这里增加一个 REGIONS 成员即可。
}