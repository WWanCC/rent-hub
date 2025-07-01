package renthub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import renthub.domain.dto.Result;
import renthub.service.FavoriteService;

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
}
