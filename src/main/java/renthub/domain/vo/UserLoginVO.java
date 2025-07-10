package renthub.domain.vo;

import cn.dev33.satoken.stp.SaTokenInfo;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserLoginVO {
    private String phone;
    private SaTokenInfo tokenInfo;
}
