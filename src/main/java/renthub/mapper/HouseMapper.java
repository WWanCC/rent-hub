package renthub.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import renthub.domain.po.House;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import renthub.domain.query.PageQuery;

/**
 * <p>
 * 房源 Mapper 接口
 * </p>
 *
 * @author Bai5
 * @since 2025-06-25
 */
public interface HouseMapper extends BaseMapper<House> {
    IPage<House> listHouseIdsByCondition(PageQuery pQuery);
}
