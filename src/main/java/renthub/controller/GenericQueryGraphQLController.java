package renthub.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import renthub.GraphQL.EntityAlias;
import renthub.GraphQL.EntityPage;
import renthub.GraphQL.SortInput;
import renthub.common.CommonQueryService;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class GenericQueryGraphQLController {

    private final CommonQueryService commonQueryService;

    @QueryMapping // 对应 Schema 中的 "queryEntities"
    public EntityPage queryEntities(
            @Argument EntityAlias entity,
            @Argument int page,
            @Argument int size,
            @Argument Map<String, Object> filter,
            @Argument List<SortInput> sort) {

        // 1. 调用 Service 层，获取 MyBatis-Plus 的 IPage 结果
        IPage<?> pageResult = commonQueryService.query(entity, filter, sort, page, size);

        // 2. 【核心改动 2】手动将 IPage 的数据，适配到我们的 EntityPage DTO 中
        EntityPage resultPage = new EntityPage();

        if (pageResult != null) {
            resultPage.setContent(pageResult.getRecords());
            resultPage.setTotalPages((int) pageResult.getPages());
            resultPage.setTotalElements(pageResult.getTotal());
            resultPage.setNumber((int) pageResult.getCurrent());
            resultPage.setSize((int) pageResult.getSize());
        }
        else {
            // 如果 service 返回 null，提供一个空的默认值，避免空指针
            resultPage.setContent(List.of());
        }

        return resultPage;
//        // 直接将所有参数透传给我们的通用 Service
//        return commonQueryService.query(entity, filter, sort, page, size);
    }
}