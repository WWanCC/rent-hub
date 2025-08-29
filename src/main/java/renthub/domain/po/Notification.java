package renthub.domain.po;

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
 * 
 * </p>
 *
 * @author Bai5
 * @since 2025-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 引用user表逻辑外键
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 引用emp表逻辑外键
     */
    @TableField("emp_id")
    private Integer empId;

    /**
     * 通知标题
     */
    @TableField("title")
    private String title;

    /**
     * 通知内容   
     */
    @TableField("content")
    private String content;

    /**
     *   创建时间 
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 默认PENDING,把信息插入通知表后,立即发送给CB端,之后修改为sent状态
     */
    @TableField("delivery_status")
    private String deliveryStatus;

    @TableField("user_is_read")
    private Integer userIsRead;

    @TableField("emp_is_read")
    private Integer empIsRead;


}
