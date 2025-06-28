package renthub.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import renthub.domain.po.Result;
import renthub.domain.query.PageQuery;
import renthub.domain.vo.HouseListVO;
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

//    @GetMapping
//    public Result list() {
//        List<House> list = houseService.list();
//        return Result.success(list);
//    }

    @GetMapping("list")
    public Result<IPage<HouseListVO>> findHouseByPage(PageQuery pQuery) {
        IPage<HouseListVO> houseList = houseService.findHouseByPage(pQuery);
        return Result.success(houseList);
    }
}
