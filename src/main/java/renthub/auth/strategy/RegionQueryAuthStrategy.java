package renthub.auth.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;
import renthub.domain.po.Region;

@Component
public class RegionQueryAuthStrategy implements QueryAuthStrategy<Region> {
    @Override
    public Class<Region> getEntityClass() { return Region.class; }

    @Override
    public void applyAuth(QueryWrapper<Region> wrapper) {
        // 地区信息通常是公开的，所以无需任何操作
    }
}