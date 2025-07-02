package renthub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import renthub.domain.po.UserFavorite;
import renthub.domain.vo.HouseVO;

import java.util.List;

public interface FavoriteService extends IService<UserFavorite> {
    void addFavorite(Integer houseId);
    List<HouseVO> listAllUserFavorites();
}
