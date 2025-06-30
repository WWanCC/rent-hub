package renthub.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import renthub.convert.PageConverter;
import renthub.domain.dto.PageResult;
import renthub.domain.dto.Result;
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
@RequestMapping("/house")
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;
    private final PageConverter pageConverter;

//    @GetMapping
//    public Result list() {
//        List<House> list = houseService.list();
//        return Result.success(list);
//    }


    //用于 多条件分页查询
    @GetMapping("pageList")
    public Result<PageResult<HouseVO>> searchHouseByPage(@Validated PageQuery pQuery) {
        IPage<HouseVO> PageHouseListVO = houseService.findHouseByPage(pQuery);
        PageResult<HouseVO> pageResult = pageConverter.toPageResult(PageHouseListVO);
        return Result.success(pageResult);
    }
}
