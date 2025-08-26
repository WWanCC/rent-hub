package renthub.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import renthub.domain.dto.AdminCreateContractDTO;
import renthub.domain.po.House;
import renthub.domain.po.RentalContract;
import renthub.enums.HouseStatusEnum;
import renthub.mapper.HouseMapper;
import renthub.mapper.RentalContractMapper;
import renthub.service.IRentalContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租赁合同表 服务实现类
 * </p>
 *
 * @author Bai5
 * @since 2025-08-26
 */
@Service
@RequiredArgsConstructor
public class RentalContractServiceImpl extends ServiceImpl<RentalContractMapper, RentalContract> implements IRentalContractService {

    private HouseMapper houseMapper;

    @Override
    @Transactional // 开启事务，保证数据一致性
    public String createContractForUser(AdminCreateContractDTO dto, int currentAdminId) {
        // 1. [调用Mapper] 以悲观锁查询房源，防止并发操作
        House house = houseMapper.selectByIdForUpdate(dto.getHouseId());

        // 2. [业务校验]
        if (house == null) {
            throw new RuntimeException("房源不存在");
        }
        if (house.getStatus() != HouseStatusEnum.AVAILABLE.getCode()) {
            throw new RuntimeException("该房源已被预定或签约，无法创建合同");
        }

        // 3. [业务操作] 施加业务锁：更新房源状态为“待用户确认”  LOCKED状态
        house.setStatus(HouseStatusEnum.LOCKED.getCode());
        houseMapper.updateById(house);

        // 4. [业务操作] 创建合同实体，并填充数据
        RentalContract contract = new RentalContract()
                .setContractNo(generateContractNo()) // 生成唯一的合同编号
                .setHouseId(dto.getHouseId())
                .setUserId(dto.getUserId())
                .setEmpId(currentAdminId) // 创建合同的中介ID
                .setFinalPrice(dto.getFinalPrice())
                .setStartDate(dto.getStartDate())
                .setEndDate(dto.getEndDate())
                .setStatus(1); // 合同状态 (1:进行中, 2:签约完成)

        // 5. [调用Mapper] 插入合同数据
        this.save(contract); 

        // 6. [返回结果] 将新生成的合同编号返回给 Controller
        return contract.getContractNo();
    }

    /**
     * 生成一个唯一的合同编号 (私有辅助方法)
     */
    private String generateContractNo() {
        String datePart = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")
                .format(java.time.LocalDate.now());
        // 使用时间戳的后6位作为随机部分，简单且几乎不会重复
        String timePart = String.valueOf(System.currentTimeMillis()).substring(7);
        return "HC-" + datePart + "-" + timePart;
    }
}
