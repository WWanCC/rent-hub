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

}
