package renthub.convert;

import org.mapstruct.Mapper;
import renthub.domain.dto.UpsertHouseDTO;
import renthub.domain.po.House;

@Mapper(componentModel = "spring")
public interface HouseConverter2 {
    House toPo(UpsertHouseDTO upsertHouseDTO);
}
