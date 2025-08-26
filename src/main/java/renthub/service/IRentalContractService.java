package renthub.service;

import jakarta.validation.Valid;
import renthub.domain.dto.AdminCreateContractDTO;
import renthub.domain.po.RentalContract;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 租赁合同表 服务类
 * </p>
 *
 * @author Bai5
 * @since 2025-08-26
 */
public interface IRentalContractService extends IService<RentalContract> {
    /**
     * 由中介为指定用户创建一份“待确认”的合同
     * @param dto 包含合同核心信息的DTO
     * @return 新创建的合同唯一编号 (String)
     */
    String createContractForUser(@Valid AdminCreateContractDTO dto, int currentAdminId);
}
