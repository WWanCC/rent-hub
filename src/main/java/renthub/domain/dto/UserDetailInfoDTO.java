package renthub.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDetailInfoDTO {
    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 6, message = "真实姓名长度不能超过6位")
    private String realName;

    @NotBlank(message = "身份证号不能为空")
    @Size(min = 18, max = 18, message = "身份证号长度必须为18位")
    private String identityCardId;
}
