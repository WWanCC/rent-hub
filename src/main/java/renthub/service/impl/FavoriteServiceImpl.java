package renthub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renthub.domain.po.House;
import renthub.domain.po.UserFavorite;
import renthub.domain.vo.HouseVO;
import renthub.enums.BusinessExceptionStatusEnum;
import renthub.enums.HouseStatusEnum;
import renthub.exception.BusinessException;
import renthub.exception.SystemException;
import renthub.mapper.FavoriteMapper;
import renthub.service.FavoriteService;
import renthub.service.HouseService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, UserFavorite> implements FavoriteService {
    private final HouseService houseService;

    /**
     * 添加房源到收藏夹
     *
     * @param houseId 要收藏的房源ID
     */
    @Transactional
    @Override
    public void addFavorite(Integer houseId) {
        // TODO 获取当前用户ID
        //暂时写死
        Integer userId = 1;

        // 检查被收藏房源 是否(存在) 待租状态
        LambdaQueryWrapper<House> houseExistWrapper = new LambdaQueryWrapper<>();
        houseExistWrapper.eq(House::getId, houseId).eq(House::getStatus, HouseStatusEnum.AVAILABLE);
//        健壮性检查：如果被收藏的房源不存在，或者不是待租状态，则无法收藏
        if (!houseService.exists(houseExistWrapper)) {
            throw new BusinessException(BusinessExceptionStatusEnum.ResourceNotFoundException, "被收藏的房源不存在或者不是待租状态");
        }

        //检查用户是否已经收藏过此房源 ，不要依靠数据库的联合主键，使用数据库抛出异常作为判断是反模式
        LambdaQueryWrapper<UserFavorite> favoriteExistWrapper = new LambdaQueryWrapper<>();
        favoriteExistWrapper
                .eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getHouseId, houseId);

        if (this.exists(favoriteExistWrapper)) {
            throw new BusinessException(BusinessExceptionStatusEnum.DuplicateResource, "已经收藏过此房源");
        }

//        所有校验通过后，开始添加收藏
        UserFavorite userFavorite = new UserFavorite(userId, houseId, LocalDateTime.now());

        //防御的不是业务逻辑错误，而是：并发冲突（如唯一键重复插入），数据完整性（由数据库的NOT NULL等约束强制保证），系统级的意外（如网络、数据库硬件等基础设施问题）等
        try {
            if (!this.save(userFavorite)) {
                // save() 返回 false，这是一个非预期的持久化失败
                throw new SystemException("保存收藏记录失败，数据库未返回受影响行数");
            }
        } catch (DataAccessException e) {
            // 【更重要】捕获由数据库约束（如唯一键）触发的底层异常
            // Spring 会将底层的JDBCException转换为DataAccessException的子类
            log.error("收藏记录插入数据库时发生数据访问异常", e); // 记录原始异常
            throw new SystemException("收藏失败，请稍后重试", e); // 抛出我们自己的系统异常，并包裹原始异常
        }
    }

    @Override
    public List<HouseVO> listAllUserFavorites() {
        //TODO 获取当前用户ID
        Integer userId = getCurrentUserId();
        return this.baseMapper.findAllUserFavorite(userId);
    }

    private Integer getCurrentUserId() {
        // 示例实现，请替换为您的真实逻辑
        //TODO 是否需要获取当前用户ID的方法?
        return 1;
    }
}
