package renthub.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import renthub.domain.po.House;
import com.baomidou.mybatisplus.extension.service.IService;
import renthub.domain.query.PageQuery;
import renthub.domain.vo.HouseListVO;

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

    IPage<HouseListVO> findHouseByPage(PageQuery pQuery);


}