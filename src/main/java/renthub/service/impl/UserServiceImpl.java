package renthub.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renthub.auth.StpKit;
import renthub.domain.dto.UserProfileDTO;
import renthub.domain.dto.UserDetailInfoDTO;
import renthub.domain.dto.UserLoginDTO;
import renthub.domain.po.User;
import renthub.domain.po.UserDetail;
import renthub.domain.vo.UserProfileVO;
import renthub.enums.BusinessExceptionStatusEnum;
import renthub.enums.UserStatusEnum;
import renthub.exception.BusinessException;
import renthub.exception.SystemException;
import renthub.mapper.Template.UserDetailMapper;
import renthub.mapper.UserMapper;
import renthub.service.UserService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;
    private final UserDetailMapper userDetailMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(UserLoginDTO registerDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, registerDTO.getPhone());
        User existUser = userMapper.selectOne(wrapper);
//        检查是否存在这个用户（以手机号判断）
        if (existUser != null) {
            throw new BusinessException(BusinessExceptionStatusEnum.PHONE_ALREADY_REGISTERED, "账号已存在");
        }
//  注册：插入一个用户 的 基本信息
        User newUser = User.builder().status(UserStatusEnum.NORMAL.getCode()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .phone(registerDTO.getPhone())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .build();
        int rowsAffected = userMapper.insert(newUser);
        if (rowsAffected != 1) {
            throw new SystemException("用户注册数据保存失败");
        }
        log.debug("用户注册成功，用户账户：{}，用户密码：{}", newUser.getPhone(), newUser.getPassword());
    }

    @Override
    public SaTokenInfo login(UserLoginDTO loginDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, loginDTO.getPhone())
                .eq(User::getStatus, UserStatusEnum.NORMAL);
//        获取用户 行
        User user = userMapper.selectOne(wrapper);
        //为了安全起见，不对用户不存在和密码错误做区分，统一返回“手机号或密码错误”
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(BusinessExceptionStatusEnum.INVALID_CREDENTIALS, "用户名或密码错误");
        }
        //使用StpKit.USER 登录
        StpKit.USER.login(user.getId(), new SaLoginParameter()
                .setExtra("phone", user.getPhone())
                .setExtra("userId", user.getId())
        );

        log.debug("用户登录成功，用户账户：{}，用户token：{}", user.getPhone(), StpKit.USER.getTokenValue());
        return StpKit.USER.getTokenInfo();// 获取当前 'user' 体系的 Token
    }

    //注销 登出
    @Override
    public void logout() {
        StpKit.USER.logout();
    }

    @Override
    public void completeUserDetailInfo(UserDetailInfoDTO userDetailInfoDTO) {
        Integer userId = StpKit.USER.getLoginIdAsInt();

        LambdaQueryWrapper<UserDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDetail::getUserId, userId);
        if (userDetailMapper.exists(wrapper)) {
            throw new BusinessException(HttpStatus.CONFLICT.value(), "用户详情信息已填写");
        }

        UserDetail userDetail = new UserDetail().setUserId(userId)
                .setRealName(userDetailInfoDTO.getRealName()).setIdentityCardId(userDetailInfoDTO.getIdentityCardId());
        userDetailMapper.insert(userDetail);
    }

    @Override
    @Transactional
    public void updateUserProfile(UserProfileDTO update) {
        log.debug("进入{}", "updateUserProfile");
        Integer userId = StpKit.USER.getLoginIdAsInt();
        LambdaUpdateWrapper<UserDetail> userDetailWrapper = new LambdaUpdateWrapper<>();
        userDetailWrapper.eq(UserDetail::getUserId, userId)
                .set(update.getRealName() != null, UserDetail::getRealName, update.getRealName());

        if (userDetailWrapper.getSqlSet() != null && !userDetailWrapper.getSqlSet().isEmpty()) {
            userDetailMapper.update(null, userDetailWrapper);
        }
        log.debug("执行{}", " userDetailMapper.update");
        LambdaUpdateWrapper<User> userWrapper = new LambdaUpdateWrapper<>();
        userWrapper.eq(User::getId, userId)
                .set(update.getPhone() != null, User::getPhone, update.getPhone())
                .set(User::getUpdatedAt, LocalDateTime.now());
        if (update.getPassword() != null) {
            userWrapper.set(User::getPassword, passwordEncoder.encode(update.getPassword()));
        }
        if (userWrapper.getSqlSet() != null && !userWrapper.getSqlSet().isEmpty()) {
            userMapper.update(null, userWrapper);
        }
        log.debug("执行{}", " userMapper.update");
        log.debug("离开{}", "updateUserProfile");
    }

    @Override
    public Boolean isCompleteUserProfile() {
        Integer userId = StpKit.USER.getLoginIdAsInt();
        return userDetailMapper.exists(new LambdaQueryWrapper<UserDetail>().eq(UserDetail::getUserId, userId));
    }

    @Override
    public UserProfileVO getUserProfile() {
        Integer userId = StpKit.USER.getLoginIdAsInt();
        return userMapper.queryPhoneRealNameIdentityCardIdByUserIdAfter(userId);
    }
}
