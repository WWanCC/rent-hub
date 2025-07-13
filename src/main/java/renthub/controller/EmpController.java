package renthub.controller;


import cn.dev33.satoken.annotation.SaCheckRole;
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
import renthub.domain.po.Role;
import renthub.domain.vo.EmpLoginVO;
import renthub.domain.vo.RolePermissionsVO;
import renthub.enums.LoginTypeEnum;
import renthub.service.EmpService;
import renthub.service.RolePermissionService;
import renthub.service.RoleService;

import java.util.List;

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
    private final RoleService roleService;
    private final RolePermissionService rolePermissionService;

    @PostMapping("creat-account")
    @SaCheckRole(type = LoginTypeEnum.EmpType, value = "BranchManager")
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
        empService.logout();
        return ResponseEntity.ok(Result.success());
    }

    @GetMapping("/role")
    public ResponseEntity<Result<List<Role>>> getRoles() {
        List<Role> roles = roleService.getRoles();
        return ResponseEntity.ok(Result.success(roles));
    }

    @GetMapping("/role/{roleId}/permissions")
    public ResponseEntity<Result<RolePermissionsVO>> getRolePermissions(@PathVariable Integer roleId) {
        RolePermissionsVO rolePermissions = rolePermissionService.getRolePermissions(roleId);
        return ResponseEntity.ok(Result.success(rolePermissions));
    }
}
