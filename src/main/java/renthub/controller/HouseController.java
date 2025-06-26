package renthub.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import renthub.domain.po.House;
import renthub.domain.po.Result;
import renthub.service.HouseService;

import java.util.List;

/**
 * <p>
 * 房源 前端控制器
 * </p>
 *
 * @author Bai5
 * @since 2025-06-25
 */
@RestController
@RequestMapping("/house")
public class HouseController {
    @Autowired
    private HouseService houseService;

    @GetMapping
    public Result list() {
        List<House> list = houseService.list();
        return Result.success(list);
    }
}
