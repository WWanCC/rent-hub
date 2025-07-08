package renthub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import renthub.convert.RegionConverter;
import renthub.domain.po.Region;
import renthub.domain.vo.RegionVO;
import renthub.mapper.Template.RegionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import renthub.service.RegionService;

import java.util.List;

/**
 * <p>
 * 地区表  服务实现类
 * </p>
 *
 * @author Bai5
 * @since 2025-07-01
 */
@Service
@RequiredArgsConstructor
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {
    private final RegionConverter regionConverter;

    @Override
    public List<RegionVO> listAll() {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        //只查询需要的
        wrapper.select(Region::getId, Region::getName).orderByAsc(Region::getId);
        List<Region> regions = this.list(wrapper);
        return regionConverter.toRegionVOList(regions);
    }
}
