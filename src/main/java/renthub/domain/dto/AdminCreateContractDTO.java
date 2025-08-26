package renthub.domain.dto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AdminCreateContractDTO {

    @NotNull(message = "必须指定房源ID")
    private Integer houseId;

    @NotNull(message = "必须指定租客ID")
    private Integer userId;

    @NotNull(message = "必须填写成交租金")
    private BigDecimal finalPrice;

    @NotNull(message = "合同开始日期不能为空")
    @FutureOrPresent(message = "合同开始日期不能是过去的日期")
    private LocalDate startDate;

    @NotNull(message = "合同结束日期不能为空")
    @Future(message = "合同结束日期必须是未来的日期")
    private LocalDate endDate;
}