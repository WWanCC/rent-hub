package renthub.domain.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 租赁合同表
 * </p>
 *
 * @author Bai5
 * @since 2025-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity // <-- 【新增】告诉 JPA 这是一个实体类
@TableName("rental_contract")
public class RentalContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id // <-- 【新增】告诉 JPA 这是主键
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 合同编号, 唯一,带特殊格式的字符串
     */
    private String contractNo;

    /**
     * 外键, 关联 house_.id
     */
    private Integer houseId;

    /**
     * 外键, 关联 user.id
     */
    private Integer userId;

    /**
     * 外键, 关联 emp.id
     */
    private Integer empId;

    /**
     * 实际月租金
     */
    private BigDecimal finalPrice;

    /**
     * 合同开始日期
     */
    private LocalDate startDate;

    /**
     * 合同结束日期
     */
    private LocalDate endDate;

    /**
     * 合同状态
     */
    private Integer status;

    /**
     * 签约时间
     */
    private LocalDateTime createdAt;


}
