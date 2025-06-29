package renthub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import renthub.domain.dto.HouseTagDTO;
import renthub.domain.po.Tag;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    List<HouseTagDTO> findTagByHouseIds(@Param("houseIds") List<Integer> houseIds);
}
