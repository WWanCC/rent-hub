package renthub.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import renthub.domain.dto.Result;
import renthub.domain.query.UserProfileQuery;
import renthub.domain.query.UserDetailInfoQuery;
import renthub.domain.dto.UserLoginDTO;
import renthub.domain.vo.UserLoginVO;
import renthub.domain.vo.UserProfileVO;
import renthub.service.UserService;

/**
 * 用戶
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Validated UserLoginDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success();
    }

    //post方法可以配合https加密，且对服务器产生影响（不同token,不具备幂等性)，因此使用post
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody @Validated UserLoginDTO loginDTO) {
        SaTokenInfo tokenInfo = userService.login(loginDTO);
        UserLoginVO userLoginVO = new UserLoginVO().setPhone(loginDTO.getPhone()).setTokenInfo(tokenInfo);
        return Result.success(userLoginVO);
    }

    //注销 登出
    @PostMapping("/logout")
    public Result<Void> logout() {
        userService.logout();
        return Result.success();
    }

//    @GetMapping("loginId")
//    public Result<Object> getLoginId() {
//        StpKit.USER.isLogin();
//        StpKit.USER.getTokenValue();
//        log.warn("用户登录是否：{}", StpKit.USER.isLogin());
//        log.warn("用户token：{}", StpKit.USER.getTokenValue());
//        log.warn("用户LoginId：{}", StpKit.USER.getLoginId());
//        return Result.success(StpKit.USER.getTokenValue());
//    }

    @PostMapping("detailInfo")
    public Result<Void> detailInfo(@RequestBody @Validated UserDetailInfoQuery userDetailInfoQuery) {
        userService.completeUserDetailInfo(userDetailInfoQuery);
        return Result.success();
    }

    @GetMapping("userProfile")
    public Result<UserProfileVO> getUserProfile() {
        UserProfileVO userProfile = userService.getUserProfile();
        return Result.success(userProfile);
    }

    @PutMapping("userProfile")
    public Result<Void> updateUserProfile(@RequestBody @Validated UserProfileQuery updateDetail) {
        userService.updateUserProfile(updateDetail);
        log.debug("更新用户详情成功");
        return Result.success();
    }

    @GetMapping("isCompleteUserProfile")
    public Result<Boolean> isCompleteUserProfile() {
        return Result.success(userService.isCompleteUserProfile());
    }
}
