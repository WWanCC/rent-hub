package renthub.auth;

import cn.dev33.satoken.stp.StpInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import renthub.enums.LoginTypeEnum;
import renthub.mapper.EmpAuthMapper;
import renthub.mapper.UserAuthMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final UserAuthMapper userAuthMapper;
    private final EmpAuthMapper empAuthMapper;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        if (LoginTypeEnum.USER.getCode().equals(loginType)) {
            Integer userId = Integer.parseInt(loginId.toString());
            return userAuthMapper.findPermissionKeysByUserId(userId);
        } else if (LoginTypeEnum.EMP.getCode().equals(loginType)) {
            Integer empId = Integer.parseInt(loginId.toString());
            return empAuthMapper.findPermissionKeysByEmpId(empId);
        }
        return Collections.emptyList(); // 返回空集合，防止空指针
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        if (LoginTypeEnum.USER.getCode().equals(loginType)) {
            Integer userId = Integer.parseInt(loginId.toString());
            return userAuthMapper.findRoleKeysByUserId(userId);
        } else if (LoginTypeEnum.EMP.getCode().equals(loginType)) {
            Integer empId = Integer.parseInt(loginId.toString());
            return empAuthMapper.findRoleKeysByEmpId(empId);
        }
        return Collections.emptyList(); // 返回空集合，防止空指针
    }

}
