package renthub.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import renthub.GraphQL.EntityAlias;
import renthub.GraphQL.SortInput;
import renthub.common.CommonQueryService;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class GenericQueryGraphQLController {

    private final CommonQueryService commonQueryService;

    @QueryMapping // 对应 Schema 中的 "queryEntities"
    public IPage<?> queryEntities(
            @Argument EntityAlias entity,
            @Argument int page,
            @Argument int size,
            @Argument Map<String, Object> filter,
            @Argument List<SortInput> sort) {

        // 直接将所有参数透传给我们的通用 Service
        return commonQueryService.query(entity, filter, sort, page, size);
    }
}