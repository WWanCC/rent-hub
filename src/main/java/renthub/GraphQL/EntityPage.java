package renthub.GraphQL;

import lombok.Data;
import java.util.List;

@Data
public class EntityPage {
    private List<?> content; // 用泛型 ? 接收任意类型的列表
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;
}