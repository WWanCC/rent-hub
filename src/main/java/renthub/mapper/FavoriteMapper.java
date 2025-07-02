package renthub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import renthub.domain.po.UserFavorite;
import renthub.domain.vo.HouseVO;

import java.util.List;

public interface FavoriteMapper extends BaseMapper<UserFavorite> {
    List<HouseVO> findAllUserFavorite(@Param("userId") Integer userId);
}
