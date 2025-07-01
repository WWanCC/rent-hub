package renthub.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import renthub.domain.po.Region;
import renthub.mapper.RegionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地区表  服务实现类
 * </p>
 *
 * @author Bai5
 * @since 2025-07-01
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IService<Region> {

}
