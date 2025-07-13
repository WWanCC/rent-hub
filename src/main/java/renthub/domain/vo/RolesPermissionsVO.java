package renthub.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import renthub.domain.po.Permission;
import renthub.domain.po.Role;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolesPermissionsVO {
    private List<Role> roles;
    private List<Permission> permissions;
}
