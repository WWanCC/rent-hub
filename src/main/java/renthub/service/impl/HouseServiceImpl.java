package renthub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import renthub.convert.HouseConverter;
import renthub.domain.dto.HouseTagDTO;
import renthub.domain.po.House;
import renthub.domain.po.Tag;
import renthub.domain.query.PageQuery;
import renthub.domain.vo.HouseListVO;
import renthub.enums.HouseStatusEnum;
import renthub.mapper.HouseMapper;
import renthub.mapper.TagMapper;
import renthub.service.HouseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
    private final HouseConverter houseConverter;

    @Override //这个业务的总方法
    public IPage<HouseListVO> findHouseByPage(PageQuery pQuery) {
        //声明分页
        IPage<House> page = new Page<>(pQuery.getPageNum(), pQuery.getPageSize());
        //第一次查询
        List<Integer> houseIds = houseMapper.findListByQuery(page, pQuery);
        // 健壮性判断：如果一页的ID都查不出来，说明没有数据，直接返回一个空的分页对象
        //未验证
        if (CollectionUtils.isEmpty(houseIds)) {
            return new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        }
//        第二次查询 获取分页houseId对应的信息
        List<House> houseList = this.listByIds(houseIds);
        //第三次查询
        List<HouseTagDTO> htDAOList = tagMapper.findTagByHouseIds(houseIds);

        //组装数据
        Map<Integer, List<Tag>> collect = htDAOList.stream().collect(Collectors.groupingBy(HouseTagDTO::getHouseId,
                Collectors.mapping(dto -> {
                    Tag tag = new Tag();
                    tag.setId(dto.getTagId());
                    tag.setName(dto.getTagName());
                    return tag;
                }, Collectors.toList())// 收集成Tag列表
        ));

//        转换为HouseListVO
        List<HouseListVO> voList = houseConverter.toVoList(houseList, collect);

        return null;
    }
}



