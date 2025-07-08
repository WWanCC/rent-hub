package renthub.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface UserAuthMapper {
    /**
     * 根据用户ID查询其所有权限标识
     */
    List<String> findPermissionKeysByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户ID查询其所有角色标识
     */
    List<String> findRoleKeysByUserId(@Param("userId") Integer userId);
}
