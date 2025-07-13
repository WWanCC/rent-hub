package renthub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import renthub.domain.po.EmpRole;
import renthub.domain.po.Role;
import renthub.mapper.RoleMapper;
import renthub.mapper.Template.EmpRoleMapper;
import renthub.service.EmpRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 员工-角色中间表 服务实现类
 * </p>
 *
 * @author Bai5
 * @since 2025-07-08
 */
@Service
@RequiredArgsConstructor
public class EmpRoleServiceImpl extends ServiceImpl<EmpRoleMapper, EmpRole> implements EmpRoleService {

    private final EmpRoleMapper empRoleMapper;
    private final RoleMapper roleMapper;

    @Override
    public List<Role> getRolesByEmpId(Integer empId) {
        LambdaQueryWrapper<EmpRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmpRole::getEmpId, empId).select(EmpRole::getRoleId);

        //获取这个员工id的全部roleId
        List<Object> roleIdsAsObject = empRoleMapper.selectObjs(wrapper);

        // 如果该员工没有任何角色，直接返回一个空列表
        if (roleIdsAsObject == null || roleIdsAsObject.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> roleIds = roleIdsAsObject.stream().map(obj -> (Integer) obj).toList();
        return roleMapper.selectBatchIds(roleIds);
    }
}
