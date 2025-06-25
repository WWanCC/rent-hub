package renthub.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import renthub.po.House;
import renthub.po.Restult;
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
    public Restult list() {
        List<House> list = houseService.list();
        System.out.println("热重载");
        return Restult.success(list);
    }
}
