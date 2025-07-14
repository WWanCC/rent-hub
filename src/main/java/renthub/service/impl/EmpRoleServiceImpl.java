package renthub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import renthub.domain.po.Emp;
import renthub.domain.po.EmpRole;
import renthub.domain.po.Role;
import renthub.domain.vo.RolesPermissionsVO;
import renthub.enums.BusinessExceptionStatusEnum;
import renthub.exception.BusinessException;
import renthub.mapper.EmpMapper;
import renthub.mapper.RoleMapper;
import renthub.mapper.Template.EmpRoleMapper;
import renthub.service.EmpRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import renthub.service.EmpService;

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
    private final EmpService empService;

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

    @Override
    @Transactional
    public RolesPermissionsVO updateEmpRoles(Integer empId, List<Integer> roleIds) {
        LambdaQueryWrapper<Emp> empWrapper = new LambdaQueryWrapper<>();
        empWrapper.eq(Emp::getId, empId);
        if (!empService.exists(empWrapper)) {
            throw new BusinessException(BusinessExceptionStatusEnum.EMP_NOT_EXIST, "该员工不存在");
        }


        if (roleIds != null && !roleIds.isEmpty()) {
            LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
            roleWrapper.in(Role::getId, roleIds);
            if (roleMapper.selectCount(roleWrapper) != roleIds.size()) {
                throw new BusinessException(BusinessExceptionStatusEnum.ROLE_NOT_EXIST, "包含错误的角色");
            }
        }

        LambdaQueryWrapper<Emp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Emp::getId, empId);
        empService.remove(wrapper);

        if (roleIds != null && !roleIds.isEmpty()) {
            List<EmpRole> batchList = roleIds.stream().map(roleId -> {
                EmpRole er = new EmpRole();
                er.setEmpId(empId);
                er.setRoleId(roleId);
                return er;
            }).toList();

            this.saveBatch(batchList);
        }
        return empService.getEmpRolesPermissions(empId);
    }
}
