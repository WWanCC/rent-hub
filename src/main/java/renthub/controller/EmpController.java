package renthub.controller;


import cn.dev33.satoken.stp.SaTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import renthub.auth.StpKit;
import renthub.domain.dto.EmpLoginDTO;
import renthub.domain.dto.Result;
import renthub.domain.po.Emp;
import renthub.domain.vo.EmpLoginVO;
import renthub.service.EmpService;

/**
 * 后台管理
 *
 * @author Bai5
 * @since 2025-06-25
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class EmpController {
    private final PasswordEncoder passwordEncoder;
    private final EmpService empService;

    @PostMapping("creat-account")
    public ResponseEntity<Result<Void>> createAccount(@RequestBody Emp emp) {
        emp.setPassword(passwordEncoder.encode(emp.getPassword()));
        empService.save(emp);
        return new ResponseEntity<>(Result.success(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Result<EmpLoginVO>> login(@RequestBody @Validated EmpLoginDTO empLoginDTO) {
        SaTokenInfo tokenInfo = empService.login(empLoginDTO);
        EmpLoginVO EmpLoginVO = new EmpLoginVO().setUsername(empLoginDTO.getUsername()).setTokenInfo(tokenInfo);
        return ResponseEntity.ok(Result.success(EmpLoginVO));
    }

    @GetMapping("/logout")
    public ResponseEntity<Result<Void>> logout() {
        StpKit.EMP.logout();
        return ResponseEntity.ok(Result.success());
    }
}
