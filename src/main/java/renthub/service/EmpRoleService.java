package renthub.service;

import renthub.domain.po.EmpRole;
import com.baomidou.mybatisplus.extension.service.IService;
import renthub.domain.po.Role;
import renthub.domain.vo.RolesPermissionsVO;

import java.util.List;

/**
 * <p>
 * 员工-角色中间表 服务类
 * </p>
 *
 * @author Bai5
 * @since 2025-07-08
 */
public interface EmpRoleService extends IService<EmpRole> {

    List<Role> getRolesByEmpId(Integer empId);

    void updateEmpRoles(Integer empId, List<Integer> roleIds);


}
