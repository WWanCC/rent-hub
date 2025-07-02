package renthub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import renthub.domain.dto.Result;
import renthub.domain.vo.HouseVO;
import renthub.service.FavoriteService;

import java.util.List;

/**
 * 收藏夹
 */

@RestController
@RequestMapping("users/favorites")
@RequiredArgsConstructor
public class FavoritesController {
    private final FavoriteService favoriteService;

    @PostMapping("/{houseId}")
    public Result<Object> addFavorite(@PathVariable Integer houseId) {
        favoriteService.addFavorite(houseId);
        // 如果Service没有抛出任何异常，就代表整个业务流程成功
        return Result.success();
    }

    @GetMapping
    public Result<List<HouseVO>> list() {
        return Result.success(favoriteService.listAllUserFavorites());
    }
}
