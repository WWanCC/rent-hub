package renthub.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import renthub.controller.PermissionDTO;
import renthub.domain.po.Permission;
import renthub.domain.po.Role;
import renthub.domain.po.RolePermission;
import renthub.domain.vo.RolePermissionsVO;
import renthub.domain.vo.RolesPermissionsVO;

import javax.xml.transform.Source;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RolePermissionConverter {

    @Mappings({
            @Mapping(source = "role.id", target = "roleId"),
            @Mapping(source = "role.name", target = "roleName"),
            @Mapping(source = "role.updatedAt", target = "updatedAt")

    })
    RolePermissionsVO toRolePermissionsVO(Role role, List<Permission> permissionList);

    @Mappings({
            @Mapping(source = "id", target = "permissionId"),
            @Mapping(source = "name", target = "permissionName"),
            @Mapping(source = "permissionKey", target = "permissionKey")
    })
    PermissionDTO toPermissionDTO(Permission permission);

    List<PermissionDTO> toPermissionDTOList(List<Permission> permissionList);


   //使用@mapping只能做到属性映射，不能做到对象映射，手动实现
    default RolesPermissionsVO toRolesPermissionsVO(List<Role> roles, List<Permission> permissions) {
        // 如果任一列表为null，可以做一些保护性处理
        if (roles == null && permissions == null) {
            return null;
        }

        // 1. 创建一个新的VO实例
        RolesPermissionsVO vo = new RolesPermissionsVO();

        // 2. 手动调用 setter 方法进行装配
        vo.setRoles(roles);
        vo.setPermissions(permissions);

        // 3. 返回装配好的VO
        return vo;
    }
}
