package renthub.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import renthub.auth.StpKit;
import renthub.domain.dto.Result;
import renthub.domain.dto.UserLoginDTO;
import renthub.domain.vo.LoginVO;
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
    public Result<LoginVO> login(@RequestBody @Validated UserLoginDTO loginDTO) {
        SaTokenInfo tokenInfo = userService.login(loginDTO);
        LoginVO loginVO = new LoginVO().setPhone(loginDTO.getPhone()).setTokenInfo(tokenInfo);
        return Result.success(loginVO);
    }

    //注销 登出
    @PostMapping("/logout")
    public Result<Void> logout() {
        userService.logout();
        return Result.success();
    }

    @GetMapping("loginId")
    public Result<Object> getLoginId() {
        StpKit.USER.isLogin();
        StpKit.USER.getTokenValue();
        log.warn("用户登录是否：{}", StpKit.USER.isLogin());
        log.warn("用户token：{}", StpKit.USER.getTokenValue());
        log.warn("用户LoginId：{}", StpKit.USER.getLoginId());
        return Result.success(StpKit.USER.getTokenValue());
    }
}
