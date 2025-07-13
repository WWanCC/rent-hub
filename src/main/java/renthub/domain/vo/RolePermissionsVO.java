package renthub.domain.vo;

import lombok.Data;
import renthub.controller.PermissionDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RolePermissionsVO {
    private String roleId;
    private String roleName;
    private List<PermissionDTO> permissionList;
    private LocalDateTime updatedAt;
}
