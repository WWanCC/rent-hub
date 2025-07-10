package renthub.mapper;

import org.apache.ibatis.annotations.Mapper;
import renthub.domain.dto.HouseTagDTO;

import java.util.List;
@Mapper
public interface HouseTagMapper {
    void insertBatch(List<HouseTagDTO> houseTagDTO);
}
