package renthub.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 管理系统菜单
 * </p>
 *
 * @author Bai5
 * @since 2025-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_menu")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父菜单ID,用于构建树结构
     */
    private Integer parentId;

    /**
     * 权限标识
     */
    private Integer permissionKey;

    /**
     * 菜单项名
     */
    private String name;

    /**
     * 前端路由路径
     */
    private String path;

    /**
     * 前端组件路径
     */
    private String component;

    /**
     * 菜单图标的类名或路径
     */
    private Integer icon;

    /**
     * 控制显示排序
     */
    private Integer orderNum;


}
