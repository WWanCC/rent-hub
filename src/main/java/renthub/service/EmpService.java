package renthub.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import renthub.domain.dto.EmpLoginDTO;
import renthub.domain.po.Emp;
import com.baomidou.mybatisplus.extension.service.IService;
import renthub.domain.po.Role;
import renthub.domain.po.User;
import renthub.domain.vo.RolePermissionsVO;
import renthub.domain.vo.RolesPermissionsVO;

import java.util.List;

/**
 * <p>
 * 后台员工 服务类
 * </p>
 *
 * @author Bai5
 * @since 2025-06-25
 */
public interface EmpService extends IService<Emp> {
    SaTokenInfo login(EmpLoginDTO empLoginDTO);

    void logout();

    RolesPermissionsVO getEmpRolesPermissions(Integer empId);

    List<User> getUserList(String keyword);


    List<Emp> getEmpList(String keyword, String role);
}
