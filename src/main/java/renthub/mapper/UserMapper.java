package renthub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import renthub.domain.po.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
