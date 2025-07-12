package renthub.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import renthub.domain.po.House;
import com.baomidou.mybatisplus.extension.service.IService;
import renthub.domain.dto.UpsertHouseDTO;
import renthub.domain.query.PageQuery;
import renthub.domain.vo.HouseVO;
import renthub.domain.vo.TopHouseVO;

import java.util.List;

/**
 * <p>
 * 房源 服务类
 * </p>
 *
 * @author Bai5
 * @since 2025-06-25
 */
public interface HouseService extends IService<House> {

    IPage<HouseVO> findHouseByPage(PageQuery pQuery);

    Long getTotalCount();

    /**
     * 获取每个区域最高价格的房源  作为推荐房源
     */
    List<TopHouseVO> listTopPriceInEachRegion();

    Integer addHouse(UpsertHouseDTO upsertHouseDTO);

    Integer updateHouse(UpsertHouseDTO upsertHouseDTO);

    Integer takedownHouse(Integer houseId);
}