package renthub.service;

import jakarta.validation.Valid;
import renthub.domain.dto.AdminCreateContractDTO;
import renthub.domain.po.RentalContract;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 租赁合同表 服务类
 *
 * @author Bai5
 * @since 2025-08-26
 */
public interface IRentalContractService extends IService<RentalContract> {
    /**
     * 由中介为指定用户创建一份“待确认”的合同
     *
     * @param dto 包含合同核心信息的DTO
     * @return 新创建的合同唯一编号 (String)
     */
    String createContractForUser(AdminCreateContractDTO dto, int currentAdminId);

    List<RentalContract> getUserContracts(int currentUserId, String sortField, String sortOrder);

    /**
     * 用户确认合同
     *
     * @param contractId    要确认的合同ID
     * @param currentUserId 执行操作的用户ID（用于权限校验）
     */
    void confirmContract(Integer contractId, int currentUserId);
}
