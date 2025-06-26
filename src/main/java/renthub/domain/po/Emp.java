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
 * 后台员工
 * </p>
 *
 * @author Bai5
 * @since 2025-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("emp")
public class Emp implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    /**
     * 员工真名
     */
    private String realName;

    private String phone;

    /**
     * 分配角色
     */
    private String role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
