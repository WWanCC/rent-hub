package renthub.convert;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import renthub.domain.po.House;
import renthub.domain.po.Tag;
import renthub.domain.vo.HouseVO;

import java.util.List;
import java.util.Map;

import java.util.Collections; //需要手动导入,编译时需要用到的包 养成好习惯

@Mapper(componentModel = "spring",
        imports = {Collections.class}) //关键！在这里告诉MapStruct需要导入Collections类)
public interface HouseConverter {
    @Mapping(source = "id", target = "id") //注意：使用复杂映射规则时（比如下面的@Context）引入上下文，
    // 有些字段无法按照字段名自动映射了，需要手动指定一下
    @Mapping(target = "tags", expression = "java(houseToTagsMap.getOrDefault(house.getId(), Collections.emptyList()))")
    HouseVO toVo(House house, @Context Map<Integer, List<Tag>> houseToTagsMap);


    /**
     * 将House列表，在tags Map的上下文中，转换为HouseListVO列表
     *
     * @param houses         源House列表
     * @param houseToTagsMap 包含标签信息的上下文Map
     * @return 转换后的HouseListVO列表
     */
    List<HouseVO> toVoList(List<House> houses, @Context Map<Integer, List<Tag>> houseToTagsMap);


}
