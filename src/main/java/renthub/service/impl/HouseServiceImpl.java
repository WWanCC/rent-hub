package renthub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import renthub.auth.StpKit;
import renthub.convert.HouseConverter;
import renthub.convert.HouseConverter2;
import renthub.domain.dto.HouseTagDTO;
import renthub.domain.po.House;
import renthub.domain.po.HouseTag;
import renthub.domain.po.Region;
import renthub.domain.po.Tag;
import renthub.domain.dto.UpsertHouseDTO;
import renthub.domain.query.PageQuery;
import renthub.domain.vo.HouseVO;
import renthub.domain.vo.TopHouseVO;
import renthub.enums.BusinessExceptionStatusEnum;
import renthub.enums.HouseStatusEnum;
import renthub.exception.BusinessException;
import renthub.mapper.HouseMapper;
import renthub.mapper.HouseTagMapper;
import renthub.mapper.RegionMapper;
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
    private final HouseConverter2 houseConverter2;
    private final HouseTagMapper houseTagMapper;
    private final RegionMapper regionMapper;

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

    @Override
    @Transactional
    public Integer addHouse(UpsertHouseDTO upsertHouseDTO) {
        int loginId = StpKit.EMP.getLoginIdAsInt();
        List<Integer> tagIds = upsertHouseDTO.getTagIds();
        LambdaQueryWrapper<House> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(House::getRegionId, upsertHouseDTO.getRegionId()).eq(House::getAddressDetail, upsertHouseDTO.getAddressDetail());
        if (this.exists(wrapper)) {
            throw new BusinessException(BusinessExceptionStatusEnum.HOUSE_EXIST, "房源已存在");
        }
        House house = houseConverter2.toPo(upsertHouseDTO);
        house.setCreatedByEmpId(loginId);
        Region region = regionMapper.selectById(upsertHouseDTO.getRegionId());
        house.setRegionName(region.getName());
        //插入房源
        this.save(house);
        Integer newHouseId = house.getId();
        List<HouseTagDTO> houseTagList = tagIds.stream().map(tagId -> {
            HouseTagDTO houseTagDTO = new HouseTagDTO();
            houseTagDTO.setHouseId(newHouseId);
            houseTagDTO.setTagId(tagId);
            return houseTagDTO;
        }).toList();
        //根据回填house.id 插入HouseTag
        houseTagMapper.insertBatch(houseTagList);
        return house.getId();
    }

    @Override
    @Transactional
    public Integer updateHouse(UpsertHouseDTO upsertHouseDTO) {
        if (upsertHouseDTO.getRegionId() != null) {
            Region region = regionMapper.selectById(upsertHouseDTO.getRegionId());
            if (region != null) {
                upsertHouseDTO.setRegionName(region.getName());
            } else {
                throw new BusinessException(BusinessExceptionStatusEnum.ResourceNotFoundException, "地区不存在"); // 抛出业务异常，返回给前端
            }
        }
//        更新house主表
        Integer affectedRows = houseMapper.updateHouseWithoutTag(upsertHouseDTO);
        if (affectedRows == 0) {
            throw new RuntimeException("更新失败，房源不存在或数据未变化，ID: " + upsertHouseDTO.getHouseId());
        }
//        更新标签 tag 关联表
        Integer houseId = upsertHouseDTO.getHouseId();
        List<Integer> tagIds = upsertHouseDTO.getTagIds();

        LambdaQueryWrapper<HouseTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HouseTag::getHouseId, houseId);
        //先删除 house实体 的全部标签
        houseTagMapper.delete(wrapper);

        //再插入新的标签
        if (tagIds != null && !tagIds.isEmpty()) {
            List<HouseTagDTO> list = tagIds.stream().map(tagId -> {
                HouseTagDTO houseTag = new HouseTagDTO();
                houseTag.setHouseId(houseId);
                houseTag.setTagId(tagId);
                return houseTag;
            }).toList();
            houseTagMapper.insertBatch(list);
        }
        return houseId;
    }

    @Override
    public Integer takedownHouse(Integer houseId) {
        LambdaUpdateWrapper<House> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(House::getId, houseId).set(House::getStatus, HouseStatusEnum.DELISTED.getCode());
        return houseMapper.update(null, wrapper);
    }
}



