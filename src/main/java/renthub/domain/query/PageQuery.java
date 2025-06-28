package renthub.domain.query;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PageQuery {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Integer regionId;
    private String keyword;
    private List<Integer> layout; //户型 房间数
    private List<Integer> tag;
    private BigDecimal minRent;
    private BigDecimal maxRent;
    private String sorted = "desc";
}
