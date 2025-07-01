package renthub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import renthub.domain.po.UserFavorite;

public interface FavoriteService extends IService<UserFavorite> {
    void addFavorite(Integer houseId);
}
