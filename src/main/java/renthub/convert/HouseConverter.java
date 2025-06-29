package renthub.convert;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import renthub.domain.po.House;
import renthub.domain.po.Tag;
import renthub.domain.vo.HouseListVO;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface HouseConverter {
    @Mapping(target = "tags", expression = "java(houseToTagsMap.getOrDefault(house.getId(), Collections.emptyList()))")
    HouseListVO toVo(House house, @Context Map<Integer, List<Tag>> houseToTagsMap);



    /**
     * 将House列表，在tags Map的上下文中，转换为HouseListVO列表
     *
     * @param houses 源House列表
     * @param houseToTagsMap 包含标签信息的上下文Map
     * @return 转换后的HouseListVO列表
     */
    List<HouseListVO> toVoList(List<House> houses, @Context Map<Integer, List<Tag>> houseToTagsMap);


}
