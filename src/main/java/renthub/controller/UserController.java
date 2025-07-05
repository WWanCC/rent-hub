package renthub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import renthub.domain.dto.Result;
import renthub.domain.dto.UserLoginDTO;
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
    public Result<String> login(@RequestBody @Validated UserLoginDTO loginDTO) {
        return Result.success(userService.login(loginDTO));
    }

}
