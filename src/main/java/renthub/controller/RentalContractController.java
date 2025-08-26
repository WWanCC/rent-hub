package renthub.controller;


import cn.dev33.satoken.stp.StpUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import renthub.auth.StpKit;
import renthub.domain.dto.AdminCreateContractDTO;
import renthub.domain.dto.Result;
import renthub.service.IRentalContractService;

/**
 * 租赁合同表
 *
 * @author Bai5
 * @since 2025-08-26
 */
@RestController
@RequestMapping("admin/rental-contract")
@RequiredArgsConstructor
public class RentalContractController {
    private final IRentalContractService contractService;


    /**
     * B端接口：中介为指定用户创建一份“待确认”的合同
     */
    @PostMapping
    public ResponseEntity<Result<String>> createContract(@RequestBody @Valid AdminCreateContractDTO dto) {
        int empId = StpKit.EMP.getLoginIdAsInt();
        String contractNo = contractService.createContractForUser(dto, empId);
        return ResponseEntity.ok(Result.success(contractNo));
    }
}
