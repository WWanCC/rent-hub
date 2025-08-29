package renthub.GraphQL;

import lombok.Data;
import jakarta.validation.constraints.Min; // 引入校验注解

/**
 * GraphQL 查询中用于分页的输入对象 DTO。
 * 与 schema.graphqls 中定义的 PaginationInput 类型完全对应。
 */
@Data
public class PaginationInput {

    /**
     * 页码，从1开始
     */
    @Min(value = 1, message = "页码必须大于等于1") // 增加校验，防止无效参数
    private int page;

    /**
     * 每页数量
     */
    @Min(value = 1, message = "每页数量必须大于等于1")
    private int size;
}