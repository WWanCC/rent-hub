package renthub.convert;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import renthub.domain.po.Region;
import renthub.domain.vo.RegionVO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RegionConverter {
    RegionVO toRegionVO(Region region);

    List<RegionVO> toRegionVOList(List<Region> regions);
}
