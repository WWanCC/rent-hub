package renthub.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import renthub.convert.HouseConverter;
import renthub.domain.dto.HouseTagDTO;
import renthub.domain.po.House;
import renthub.domain.po.Tag;
import renthub.domain.query.PageQuery;
import renthub.domain.vo.HouseVO;
import renthub.domain.vo.TopHouseVO;
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
@Slf4j
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {
    private final HouseMapper houseMapper;
    private final TagMapper tagMapper;
    private final HouseConverter houseConverter;

    @Override //这个业务的总方法
    public IPage<HouseVO> findHouseByPage(PageQuery pQuery) {
        //声明分页
        IPage<House> page = new Page<>(pQuery.getPageNum(), pQuery.getPageSize());
        //第一次查询
        IPage<Integer> listByQuery = houseMapper.findListByQuery(page, pQuery);
        List<Integer> houseIds = listByQuery.getRecords();
        // 健壮性判断：如果一页的ID都查不出来，说明没有数据，直接返回一个空的分页对象
        if (CollectionUtils.isEmpty(houseIds)) {
            return new Page<>(listByQuery.getCurrent(), listByQuery.getSize(), 0);
        }
//        第二次查询 获取分页houseId对应的信息
        List<House> houseList = this.listByIds(houseIds);
        log.debug("第二次查询，获取分页对应的house：{}", houseList.toString());
        //第三次查询
        List<HouseTagDTO> htDAOList = tagMapper.findTagByHouseIds(houseIds);
        log.debug("第三次查询，<HouseTagDTO>数据：{}", htDAOList.toString());

        //组装数据
        Map<Integer, List<Tag>> collect = htDAOList.stream().collect(Collectors.groupingBy(HouseTagDTO::getHouseId,
                Collectors.mapping(dto -> {
                    Tag tag = new Tag();
                    tag.setId(dto.getTagId());
                    tag.setName(dto.getTagName());
                    return tag;
                }, Collectors.toList())// 收集成Tag列表
        ));
//        转换为HouseVO
        List<HouseVO> voList = houseConverter.toVoList(houseList, collect);
        log.debug("组装HouseTag和House 转换后的houseVO：{}", voList.toString());
        IPage<HouseVO> finalPage = new Page<>(
                // 使用第一次查询的分页参数，后面的查询是对数据处理
                listByQuery.getCurrent(), //当前页码
                listByQuery.getSize(), // 每页数量
                listByQuery.getTotal() //符合动态条件的总条数
        );
        finalPage.setRecords(voList); //在分页对象中，存入转换后的数据
        return finalPage;
    }

    @Override
    public Long getTotalCount() {
        return this.count();
    }

    @Override
    public List<TopHouseVO> listTopPriceInEachRegion() {
        // 未来如果需要缓存，就在这里添加
        return this.baseMapper.findTopPriceHouseInEachRegion();
    }
}



