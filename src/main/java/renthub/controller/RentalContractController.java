package renthub.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import renthub.auth.StpKit;
import renthub.domain.dto.AdminCreateContractDTO;
import renthub.domain.dto.Result;
import renthub.domain.po.RentalContract;
import renthub.enums.LoginTypeEnum;
import renthub.service.IRentalContractService;

import java.util.List;

/**
 * 租赁合同表
 *
 * @author Bai5
 * @since 2025-08-26
 */
@RestController
@RequestMapping("rental-contract")
@RequiredArgsConstructor
public class RentalContractController {
    private final IRentalContractService contractService;


    /**
     * B端接口：中介为指定用户创建一份“待确认”的合同
     */
    @PostMapping("/admin")
    @SaCheckLogin(type = LoginTypeEnum.EmpType)
    public ResponseEntity<Result<String>> createContract(@RequestBody @Valid AdminCreateContractDTO dto) {
        int empId = StpKit.EMP.getLoginIdAsInt();
        String contractNo = contractService.createContractForUser(dto, empId);
        return ResponseEntity.ok(Result.success(contractNo));
    }

    /**
     * C端接口：获取当前登录用户的所有合同列表
     *
     * @return 包含合同简要信息的列表
     */
    @GetMapping("/user")
    @SaCheckLogin(type = LoginTypeEnum.UserType)
    public ResponseEntity<Result<List<RentalContract>>> getMyContracts(@RequestParam String sortField, @RequestParam String sortOrder) {
        int currentUserId = StpKit.USER.getLoginIdAsInt();
        List<RentalContract> contractList = contractService.getUserContracts(currentUserId, sortField, sortOrder);
        return ResponseEntity.ok(Result.success(contractList));
    }


    /**
     * C端接口：用户确认由中介创建的合同
     *
     * @param contractId 要确认的合同ID (注意是Integer类型的自增主键)
     * @return 操作成功的响应
     */
    @PostMapping("/user/{contractId}/confirm")
    @SaCheckLogin(type = LoginTypeEnum.UserType)
    public ResponseEntity<Result<Void>> confirmContract(@PathVariable Integer contractId) {
        int currentUserId = StpKit.USER.getLoginIdAsInt();

        // 2. 调用 Service 层，并传入两个ID，用于业务和权限校验
        contractService.confirmContract(contractId, currentUserId);

        // 3. 如果 Service 层没有抛出异常，说明操作成功
        return ResponseEntity.ok(Result.success());
    }
}
