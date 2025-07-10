package renthub.service;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import renthub.domain.query.UserProfileQuery;
import renthub.domain.query.UserDetailInfoQuery;
import renthub.domain.dto.UserLoginDTO;
import renthub.domain.po.User;
import renthub.domain.vo.UserProfileVO;

public interface UserService extends IService<User> {
    /**
     * C端用户注册功能
     *
     * @param registerDTO 注册请求数据
     */
    void register(UserLoginDTO registerDTO);

    /**
     * C端用户登录功能
     *
     * @param loginDTO 登录请求数据 (手机号和明文密码)
     * @return 登录成功后的Token字符串
     */
    SaTokenInfo login(UserLoginDTO loginDTO);

    void logout();


    void completeUserDetailInfo(UserDetailInfoQuery userDetailInfoQuery);

    void updateUserProfile(UserProfileQuery userProfileQuery);

    Boolean isCompleteUserProfile();

    UserProfileVO getUserProfile();
}
