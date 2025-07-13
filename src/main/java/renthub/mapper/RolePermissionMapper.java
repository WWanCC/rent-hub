package renthub.mapper;

import org.apache.ibatis.annotations.Param;
import renthub.domain.po.Permission;
import renthub.domain.po.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色-权限中间表 Mapper 接口
 * </p>
 *
 * @author Bai5
 * @since 2025-07-08
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    List<Permission> getPermissions(@Param("roleIds")List<Integer> roleIds);
}
