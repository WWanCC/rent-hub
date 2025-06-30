package renthub.domain.vo;

import lombok.Data;
import renthub.domain.po.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HouseVO {
    private String image;
    private String title;
    private Integer regionId;
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
    private List<Tag> tags;
    /**
     * 房间数量
     */
    private Integer layout;

    /**
     * 0下架，1待租，2已签约
     */
    private Integer status;
    private LocalDateTime updatedAt;

}
