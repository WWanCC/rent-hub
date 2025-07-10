package renthub.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import renthub.convert.PageConverter;
import renthub.domain.dto.PageResult;
import renthub.domain.dto.Result;
import renthub.domain.po.House;
import renthub.domain.dto.UpsertHouseDTO;
import renthub.domain.query.PageQuery;
import renthub.domain.vo.HouseVO;
import renthub.domain.vo.TopHouseVO;
import renthub.service.HouseService;

import java.util.List;


/**
 * 房源
 *
 * @author Bai5
 * @since 2025-06-25
 */
@RestController
@RequestMapping("/houses")
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;
    private final PageConverter pageConverter;

//    @SaCheckRole("BranchManager")
//    @SaCheckPermission("*")
//    @SaCheckLogin
    //用于 平台推荐房源(每个区域最高价格的房源)
    @GetMapping("top-by-region")
    public Result<List<TopHouseVO>> getTopPriceHousesInEachRegion() {
        List<TopHouseVO> topHouse = houseService.listTopPriceInEachRegion();
        return Result.success(topHouse);
    }


    //用于 获取总记录数
    @GetMapping //使用Object表示 records属性无实际意义
    public Result<PageResult<Object>> countHouse() {
        Long totalCount = houseService.getTotalCount();
        PageResult<Object> total = PageResult.total(totalCount);
        return Result.success(total);
    }


    //用于 多条件分页查询
    @GetMapping("search")
    public Result<PageResult<HouseVO>> searchHouseByPage(@Validated PageQuery pQuery) {
        IPage<HouseVO> PageHouseListVO = houseService.findHouseByPage(pQuery);
        PageResult<HouseVO> pageResult = pageConverter.toPageResult(PageHouseListVO);
        return Result.success(pageResult);
    }

    /**
     * 新增房源
     * @param upsertHouseDTO 用于更新和新增
     * @return houseId
     */
    @PostMapping
    public ResponseEntity<Result<Integer>> addHouse(@RequestBody UpsertHouseDTO upsertHouseDTO) {
        Integer houseId = houseService.addHouse(upsertHouseDTO);
        return new ResponseEntity<>(Result.success(houseId), HttpStatus.CREATED);
    }
}
