package renthub.domain.query;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PageParam {
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码只能从1开始")
    private Integer pageNum = 1;

    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量至少为1")
    private Integer pageSize = 10;
}
