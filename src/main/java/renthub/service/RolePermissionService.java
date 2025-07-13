package renthub.service;

import renthub.domain.po.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;
import renthub.domain.vo.RolePermissionsVO;

import java.util.List;

/**
 * <p>
 * 角色-权限中间表 服务类
 * </p>
 *
 * @author Bai5
 * @since 2025-07-08
 */
public interface RolePermissionService extends IService<RolePermission> {

    RolePermissionsVO getRolePermissions(Integer roleId);

    RolePermissionsVO updateRolePermissions(Integer roleId, List<Integer> permissionIds);
}
