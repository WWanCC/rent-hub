package renthub.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import renthub.GraphQL.EntityAlias;
import renthub.GraphQL.EntityPage;
import renthub.GraphQL.PaginationInput;
import renthub.GraphQL.SortInput;
import renthub.common.CommonQueryService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * GraphQL 的通用查询控制器 (DataFetcher)。
 * 负责接收前端的 GraphQL 查询请求，并将其分发给 CommonQueryService 处理。
 */
@Controller
@RequiredArgsConstructor
public class GenericQueryGraphQLController {

    private final CommonQueryService commonQueryService;

    /**
     * 对应 GraphQL Schema 中的 "queryEntities" 顶级查询字段。
     * 这是整个通用查询功能的唯一入口点。
     *
     * @param entity     [必需] 要查询的实体别名
     * @param pagination [可选] 分页参数。如果前端不提供此参数，则为 null，触发全量查询。
     * @param filter     [可选] 过滤条件
     * @param sort       [可选] 排序条件
     * @return 封装好的、符合 GraphQL Schema 规范的分页结果对象
     */
    @QueryMapping // 自动匹配与方法名同名的 "queryEntities"
    public EntityPage queryEntities(
            @Argument EntityAlias entity,
            @Argument PaginationInput pagination, // <-- 【核心改动】将 page, size 合并为可选的 PaginationInput
            @Argument Map<String, Object> filter,
            @Argument List<SortInput> sort) {

        // 1. 调用 Service 层，获取由 MyBatis-Plus 返回的 IPage 结果
        //    直接将所有参数透传给我们强大的通用 Service
        IPage<?> pageResult = commonQueryService.query(entity, filter, sort, pagination);

        // 2. 将 IPage (MyBatis-Plus 的分页接口) 的数据，
        //    手动适配并转换到我们自定义的、专用于 GraphQL 的 EntityPage DTO 中。
        //    这一步是必要的，因为 IPage 接口和我们的 GraphQL Type 定义不完全匹配。
        EntityPage resultPage = new EntityPage();

        if (pageResult != null) {
            resultPage.setContent(pageResult.getRecords());
            resultPage.setTotalPages((int) pageResult.getPages());
            resultPage.setTotalElements(pageResult.getTotal());
            // GraphQL 的分页通常是从0开始计数，而 Mybatis-Plus 是从1开始，这里可以根据前端约定进行调整。
            // 假设前端也从1开始，则直接转换。
            resultPage.setNumber((int) pageResult.getCurrent());
            resultPage.setSize((int) pageResult.getSize());
        } else {
            // 增加防御性编程：如果 Service 层因意外返回 null，
            // 我们提供一个空的、符合非空约束的默认值，避免 GraphQL 引擎报错。
            resultPage.setContent(Collections.emptyList());
            resultPage.setTotalPages(0);
            resultPage.setTotalElements(0);
            resultPage.setNumber(0);
            resultPage.setSize(0);
        }

        return resultPage;
    }
}