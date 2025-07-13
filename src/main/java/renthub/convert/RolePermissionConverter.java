package renthub.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import renthub.controller.PermissionDTO;
import renthub.domain.po.Permission;
import renthub.domain.po.Role;
import renthub.domain.po.RolePermission;
import renthub.domain.vo.RolePermissionsVO;

import javax.xml.transform.Source;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RolePermissionConverter {

    @Mappings({
            @Mapping(source = "role.id", target = "roleId"),
            @Mapping(source = "role.name", target = "roleName"),
            @Mapping(source = "role.updatedAt",target = "updatedAt")

    })
    RolePermissionsVO toRolePermissionsVO(Role role, List<Permission> permissionList);

    @Mappings({
            @Mapping(source = "id", target = "permissionId"),
            @Mapping(source = "name", target = "permissionName"),
            @Mapping(source = "permissionKey", target = "permissionKey")
    })
    PermissionDTO toPermissionDTO(Permission permission);

    List<PermissionDTO> toPermissionDTOList(List<Permission> permissionList);
}
