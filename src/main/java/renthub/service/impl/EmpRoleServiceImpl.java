package renthub.service.impl;

import renthub.domain.po.EmpRole;
import renthub.mapper.Template.EmpRoleMapper;
import renthub.service.IEmpRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工-角色中间表 服务实现类
 * </p>
 *
 * @author Bai5
 * @since 2025-07-08
 */
@Service
public class EmpRoleServiceImpl extends ServiceImpl<EmpRoleMapper, EmpRole> implements IEmpRoleService {

}
