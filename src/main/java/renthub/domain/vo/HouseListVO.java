package renthub.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class HouseListVO {
    private String image;
    private String title;
    private String regionName;
    /**
     * 详细地址
     */
    private String addressDetail;

    /**
     * 每月租金
     */
    private BigDecimal pricePerMonth;

    /**
     * 房源面积
     */
    private Integer area;

    /**
     * 房间数量
     */
    private Integer layout;

    /**
     * 0下架，1待租，2已签约
     */
    private Integer status;
//    private List<> createdAt;

}
