package renthub.domain.query;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileQuery {
    @Size(min = 11, max = 11, message = "手机号长度必须为11位")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Size(max = 6, message = "真实姓名长度不能超过6位")
    private String realName;

    @Pattern(regexp = "^[a-zA-Z0-9.!@#$^&_]{1,30}$",
            message = "密码只能包含字母、数字、[.!@#$^&_]，且长度不超过30位。")
    private String password;
}
