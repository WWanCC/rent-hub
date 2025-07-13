package renthub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
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

import java.time.LocalDateTime;
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
    private final RolePermissionMapper rolePermissionMapper;
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

    @Override
    @Transactional
    public RolePermissionsVO updateRolePermissions(Integer roleId, List<Integer> permissionIds) {
        LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.eq(Role::getId, roleId);
        if (!roleMapper.exists(roleWrapper)) {
            throw new BusinessException(BusinessExceptionStatusEnum.ROLE_NOT_EXIST, "角色不存在");
        }

        //删除 该角色的全部权限
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
        this.remove(wrapper);

        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<RolePermission> RolePermissionList = permissionIds.stream().map(permissionId -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                return rolePermission;
            }).toList();
            this.saveBatch(RolePermissionList);
        }

        //利用 mybatis-plus 动态sql 更新所有 非 null的字段
        Role updateRole = new Role().setUpdatedAt(LocalDateTime.now());
        roleMapper.updateById(updateRole);

        return this.getRolePermissions(roleId);
    }
}
