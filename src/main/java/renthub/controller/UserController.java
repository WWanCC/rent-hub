package renthub.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import renthub.domain.dto.Result;
import renthub.domain.dto.UserLoginDTO;
import renthub.domain.vo.LoginVO;
import renthub.service.UserService;

/**
 * 用戶
 */

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

    //post可以配合https加密，且对服务器产生影响（不同token,不具备幂等性)，因此使用post
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody @Validated UserLoginDTO loginDTO) {
        String token = userService.login(loginDTO);
        final JWT jwt = JWTUtil.parseToken(token);
        JSONObject claims = jwt.getPayload().getClaimsJson();
        LoginVO loginVO = new LoginVO().setPhone(claims.getStr("phone")).setToken(token);
        return Result.success(loginVO);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }
}
