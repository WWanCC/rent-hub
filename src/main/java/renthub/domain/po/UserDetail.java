package renthub.domain.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户详细信息
 * </p>
 *
 * @author Bai5
 * @since 2025-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_detail")
public class UserDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Integer userId;

    /**
     * 用户身份证号
     */
    @TableField(value = "identity_card_id", updateStrategy = FieldStrategy.NEVER) //标记字段不能更新
    private String identityCardId;

    /**
     * 用户真实姓名
     */
    @TableField("real_name")
    private String realName;


}
