package renthub.service.impl;

import renthub.domain.po.RolePermission;
import renthub.mapper.RolePermissionMapper;
import renthub.service.IRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色-权限中间表 服务实现类
 * </p>
 *
 * @author Bai5
 * @since 2025-07-08
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {

}
