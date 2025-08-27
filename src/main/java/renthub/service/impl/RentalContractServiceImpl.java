package renthub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import renthub.domain.dto.AdminCreateContractDTO;
import renthub.domain.po.House;
import renthub.domain.po.RentalContract;
import renthub.enums.BusinessExceptionStatusEnum;
import renthub.enums.ContractStatusEnum;
import renthub.enums.HouseStatusEnum;
import renthub.exception.BusinessException;
import renthub.mapper.HouseMapper;
import renthub.mapper.RentalContractMapper;
import renthub.service.IRentalContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import renthub.utils.SortUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

//    // 1. 定义一个 Set 来存储所有合法的数据库列名 利用mp获取数据表列名元数据
//    private static final Set<String> VALID_SORT_COLUMNS;
//
//    // 2. 使用静态代码块，在类加载时就初始化这个白名单
//    static {
//        // 获取 RentalContract 实体对应的表信息
//        TableInfo tableInfo = TableInfoHelper.getTableInfo(RentalContract.class);
//
//        // 从表信息中，获取所有字段的数据库列名，并存入 Set
//        VALID_SORT_COLUMNS = tableInfo.getFieldList().stream()
//                .map(TableFieldInfo::getColumn) // 获取数据库列名
//                .collect(Collectors.toSet());
//    }

    private final SortUtil sortUtil;

    private final HouseMapper houseMapper;

    @Override
    @Transactional // 开启事务，保证数据一致性
    public String createContractForUser(AdminCreateContractDTO dto, int currentAdminId) {
        // 1. [调用Mapper] 以悲观锁查询房源，防止并发操作
        House house = houseMapper.selectByIdForUpdate(dto.getHouseId());

        // 2. [业务校验]
        if (house == null) {
            throw new BusinessException(BusinessExceptionStatusEnum.HOUSE_NOT_FOUND);
        }
        if (house.getStatus() != HouseStatusEnum.AVAILABLE.getCode()) {
            throw new BusinessException(BusinessExceptionStatusEnum.HOUSE_NOT_AVAILABLE);
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
                .setStatus(ContractStatusEnum.PENDING_CONFIRMATION.getCode())//待用户确认
                .setCreatedAt(LocalDateTime.now());

        // 5. [调用Mapper] 插入合同数据
        this.save(contract);

        // 6. [返回结果] 将新生成的合同编号返回给 Controller
        return contract.getContractNo();
    }

    /**
     * 使用一个类型不安全的mp方法，使用白名单检查字段是否安全防止注入
     *
     * @param currentUserId 操作的用户id
     * @param sortField     前端想要的排序字段
     * @param sortOrder     前端想要的排序顺序
     * @return
     */
    @Override
    public List<RentalContract> getUserContracts(int currentUserId, String sortField, String sortOrder) {
        // 1. 查询该用户的所有合同
        QueryWrapper<RentalContract> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", currentUserId);
//        将前端传入的驼峰命名 转换为 下划线 数据库字段名
        sortUtil.applySort(wrapper, RentalContract.class, sortField, sortOrder);

//        String sortColumn = StringUtils.camelToUnderline(sortField);
//        if (VALID_SORT_COLUMNS.contains(sortColumn)) {
//            // 如果存在，则是安全的，可以用于排序
//            boolean isAsc = "asc".equalsIgnoreCase(sortOrder);
//            wrapper.orderBy(true, isAsc, sortColumn);
//        }
//        else {
//            // 如果不存在，则使用默认排序，保证程序的健壮性
//            wrapper.orderByDesc("created_at");
//        }

        return this.list(wrapper);
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

    @Override
    @Transactional
    public void confirmContract(Integer contractId, int currentUserId) {
        // 1. [数据查询] 根据合同ID查询合同信息
        RentalContract contract = this.getById(contractId);

        //  权限校验：确认合同存在，并且确实是属于当前登录用户的
        if (contract == null || !contract.getUserId().equals(currentUserId)) {
            // 抛出异常，全局异常处理器会捕获并返回给前端错误信息
            throw new BusinessException(BusinessExceptionStatusEnum.ResourceNotFoundException);
        }

        //状态校验：确认合同当前是“待用户确认”的状态
        if (contract.getStatus() != ContractStatusEnum.PENDING_CONFIRMATION.getCode()) {
            throw new BusinessException(BusinessExceptionStatusEnum.CONTRACT_INVALID_STATUS);
        }

        // 3. [数据查询] 查询关联的房源信息，并校验其“业务锁”状态
        House house = houseMapper.selectById(contract.getHouseId());

        // a. 关联数据校验：确保房源数据是存在的
        if (house == null) {
            // 这种情况通常是数据异常，需要记录日志排查
            throw new BusinessException(BusinessExceptionStatusEnum.HOUSE_NOT_FOUND);
        }

        // b. 业务锁校验：确保房源当前是被该合同“锁定”的状态
        if (house.getStatus() != HouseStatusEnum.LOCKED.getCode()) {
            throw new BusinessException(BusinessExceptionStatusEnum.HOUSE_NOT_AVAILABLE);
        }

        // --- 所有校验全部通过，可以安全地执行数据库更新 ---

        // 4. [核心操作] 更新合同状态为“进行中”
        contract.setStatus(ContractStatusEnum.IN_PROGRESS.getCode());
        this.updateById(contract);

        // 5. [核心操作] 更新房源状态为“已签约”
        house.setStatus(HouseStatusEnum.RENTED.getCode());
        houseMapper.updateById(house);
    }
}
