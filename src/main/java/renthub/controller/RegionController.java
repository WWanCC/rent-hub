package renthub.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import renthub.domain.dto.Result;
import renthub.domain.po.Region;
import renthub.domain.vo.RegionVO;
import renthub.service.RegionService;

import java.util.List;

/**
 *
 * 地区
 *
 *
 * @author Bai5
 * @since 2025-07-01
 */
@RestController
@RequestMapping("/regions")
@RequiredArgsConstructor
public class RegionController {
    private final RegionService regionService;

    @GetMapping
    public Result<List<RegionVO>> listAllRegions() {
        List<RegionVO> regionList = regionService.listAll();
        return Result.success(regionList);
    }
}
