package renthub.domain.po;

import java.io.Serial;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Entity
public class House implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Id
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
     * 0下架，1待租，2签约中，3已签约
     */
    private Integer status;

    /**
     * 创建房源的员工id
     */
    private Integer createdByEmpId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;


}
