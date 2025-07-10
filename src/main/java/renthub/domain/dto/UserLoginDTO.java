package renthub.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginDTO {
    @NotBlank(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号长度必须为11位")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    //复杂强度验证交给前端
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9.!@#$^&_]{1,30}$",
            message = "密码只能包含字母、数字、[.!@#$^&_]，且长度不超过30位。")
    private String password;
}
