package renthub.domain.vo;

import cn.dev33.satoken.stp.SaTokenInfo;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EmpLoginVO {
    private String username;
    private SaTokenInfo tokenInfo;
}
