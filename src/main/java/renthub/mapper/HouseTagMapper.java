package renthub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import renthub.domain.dto.HouseTagDTO;
import renthub.domain.po.HouseTag;

import java.util.List;
@Mapper
public interface HouseTagMapper extends BaseMapper<HouseTag> {
    void insertBatch(List<HouseTagDTO> houseTagDTO);
}
