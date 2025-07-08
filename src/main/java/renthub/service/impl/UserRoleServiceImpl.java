package renthub.service.impl;

import renthub.domain.po.UserRole;
import renthub.mapper.UserRoleMapper;
import renthub.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-角色中间表 服务实现类
 * </p>
 *
 * @author Bai5
 * @since 2025-07-08
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
