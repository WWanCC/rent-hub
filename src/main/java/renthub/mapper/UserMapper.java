package renthub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import renthub.domain.po.User;
import renthub.domain.vo.UserProfileVO;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    UserProfileVO queryPhoneRealNameIdentityCardIdByUserIdAfter(@Param("userId")Integer userId);
}
