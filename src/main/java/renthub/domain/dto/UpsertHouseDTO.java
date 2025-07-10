package renthub.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpsertHouseDTO {
    private Integer houseId;
    List<Integer> tagIds;
    private String image;

    private String title;

    private Integer regionId;
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

    /**
     * 创建房源的员工id
     */
    private Integer createdByEmpId;
}
