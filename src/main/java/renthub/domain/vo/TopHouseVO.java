package renthub.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopHouseVO {
    private Integer id;
    private String title;
    private String image;
    private String regionName;
    private BigDecimal pricePerMonth;
}
