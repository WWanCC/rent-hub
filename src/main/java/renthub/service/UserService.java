package renthub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import renthub.domain.dto.UserLoginDTO;
import renthub.domain.po.User;

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
    String login(UserLoginDTO loginDTO);
    void logout();
}
