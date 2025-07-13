package renthub.service;

import renthub.domain.po.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author Bai5
 * @since 2025-07-08
 */
public interface RoleService extends IService<Role> {
    List<Role> getRoles();
}
