package renthub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import renthub.convert.RolePermissionConverter;
import renthub.domain.po.Permission;
import renthub.domain.po.Role;
import renthub.domain.po.RolePermission;
import renthub.domain.vo.RolePermissionsVO;
import renthub.enums.BusinessExceptionStatusEnum;
import renthub.exception.BusinessException;
import renthub.mapper.RoleMapper;
import renthub.mapper.RolePermissionMapper;
import renthub.mapper.Template.PermissionMapper;
import renthub.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色-权限中间表 服务实现类
 * </p>
 *
 * @author Bai5
 * @since 2025-07-08
 */
@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final RolePermissionConverter rolePermissionConverter;

    @Override
    public RolePermissionsVO getRolePermissions(Integer roleId) {
        LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
//        不要过早优化性能，返回全部可维护性更高
//        roleWrapper.eq(Role::getId, roleId).select(Role::getId, Role::getName);
//        roleMapper.selectOne(roleWrapper);

        Role role = roleMapper.selectById(roleId);

        if (role == null) {
            throw new BusinessException(BusinessExceptionStatusEnum.ROLE_NOT_EXIST, "角色不存在");
        }

        List<Permission> permissionList = permissionMapper.selectPermissionsByRoleId(roleId);

        return rolePermissionConverter.toRolePermissionsVO(role, permissionList);
    }
}
