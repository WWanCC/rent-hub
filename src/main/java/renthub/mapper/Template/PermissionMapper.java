package renthub.mapper.Template;

import renthub.domain.po.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author Bai5
 * @since 2025-07-08
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> selectPermissionsByRoleId(Integer roleId);

    Integer countByIds(List<Integer> permissionIds);
}
