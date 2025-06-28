package renthub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import renthub.domain.po.House;
import renthub.domain.query.PageQuery;
import renthub.domain.vo.HouseListVO;
import renthub.enums.HouseStatusEnum;
import renthub.mapper.HouseMapper;
import renthub.mapper.TagMapper;
import renthub.service.HouseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 * 房源 服务实现类
 *
 * @author Bai5
 * @since 2025-06-25
 */
@Service
@RequiredArgsConstructor
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {
    private final HouseMapper houseMapper;
    private final TagMapper tagMapper;

    @Override //这个业务的总方法
    public IPage<HouseListVO> findHouseByPage(PageQuery pQuery) {
        //声明分页
        IPage<House> page = new Page<>(pQuery.getPageNum(), pQuery.getPageSize());

        //第一次查询


        //第二次查询
        //组装数据
        return null;
    }


}



