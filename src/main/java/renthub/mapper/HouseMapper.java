package renthub.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import renthub.domain.dto.UpsertHouseDTO;
import renthub.domain.po.House;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import renthub.domain.query.PageQuery;
import renthub.domain.vo.TopHouseVO;

import java.util.List;

/**
 * <p>
 * 房源 Mapper 接口
 * </p>
 *
 * @author Bai5
 * @since 2025-06-25
 */
public interface HouseMapper extends BaseMapper<House> {
    IPage<Integer> findListByQuery(IPage<?> page, @Param("pQuery") PageQuery pQuery);

    List<TopHouseVO> findTopPriceHouseInEachRegion();

    Integer updateHouseWithoutTag(UpsertHouseDTO upsertHouseDTO);
}
