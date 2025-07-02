package renthub.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import renthub.convert.PageConverter;
import renthub.domain.dto.PageResult;
import renthub.domain.dto.Result;
import renthub.domain.po.House;
import renthub.domain.query.PageQuery;
import renthub.domain.vo.HouseVO;
import renthub.service.HouseService;


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

    //用于 获取总记录数
    @GetMapping //使用Object表示 records属性无实际意义
    public Result<PageResult<Object>> countHouse() {
        Long totalCount = houseService.getTotalCount();
        PageResult<Object> total = PageResult.total(totalCount);
        return Result.success(total);
    }


    //用于 多条件分页查询
    @GetMapping("pageList")
    public Result<PageResult<HouseVO>> searchHouseByPage(@Validated PageQuery pQuery) {
        IPage<HouseVO> PageHouseListVO = houseService.findHouseByPage(pQuery);
        PageResult<HouseVO> pageResult = pageConverter.toPageResult(PageHouseListVO);
        return Result.success(pageResult);
    }
}
