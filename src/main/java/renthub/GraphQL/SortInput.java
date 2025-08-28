package renthub.GraphQL;

import lombok.Data;
import org.hibernate.query.SortDirection;

@Data
public class SortInput {
    private String field;
    private SortDirection direction; // SortDirection 是您在 Schema 中定义的那个枚举
}