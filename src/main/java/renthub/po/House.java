package renthub.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 房源
 * </p>
 *
 * @author Bai5
 * @since 2025-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("house")
public class House implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String image;

    private String title;

    private Integer regionId;

    /**
     * 转单表查询
     */
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

    /**
     * 创建房源的员工id
     */
    private Integer createdByEmpId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
