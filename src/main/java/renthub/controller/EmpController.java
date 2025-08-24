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
import renthub.domain.po.Permission;
import renthub.domain.po.Role;
import renthub.domain.po.User;
import renthub.domain.vo.EmpLoginVO;
import renthub.domain.vo.RolePermissionsVO;
import renthub.domain.vo.RolesPermissionsVO;
import renthub.enums.LoginTypeEnum;
import renthub.service.EmpRoleService;
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
    private final EmpRoleService empRoleService;

    @PostMapping("creat-account")
//    @SaCheckRole(type = LoginTypeEnum.EmpType, value = "BranchManager")
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

    /**
     * 获取 全部 角色列表
     *
     * @return
     */
    @GetMapping("/role")
    @SaCheckRole(type = LoginTypeEnum.EmpType, value = "BranchManager")
    public ResponseEntity<Result<List<Role>>> getRoles() {
        List<Role> roles = roleService.getRoles();
        return ResponseEntity.ok(Result.success(roles));
    }

    /**
     * 获取 某个 角色权限列表
     *
     * @param roleId 角色id
     * @return
     */
    @GetMapping("/role/{roleId}/permissions")
    @SaCheckRole(type = LoginTypeEnum.EmpType, value = "BranchManager")
    public ResponseEntity<Result<RolePermissionsVO>> getRolePermissions(@PathVariable Integer roleId) {
        RolePermissionsVO rolePermissions = rolePermissionService.getRolePermissions(roleId);
        return ResponseEntity.ok(Result.success(rolePermissions));
    }

    /**
     * 更新 某个 角色权限列表
     *
     * @param roleId        角色id
     * @param permissionIds 权限id列表
     * @return
     */
    @PutMapping("/role/{roleId}/permissions")
    @SaCheckRole(type = LoginTypeEnum.EmpType, value = "BranchManager")
    public ResponseEntity<Result<RolePermissionsVO>> updateRolePermissions(@PathVariable Integer roleId, @RequestBody List<Integer> permissionIds) {
        RolePermissionsVO rolePermissionsVO = rolePermissionService.updateRolePermissions(roleId, permissionIds);
        return ResponseEntity.ok(Result.success(rolePermissionsVO));
    }

    /**
     * 获取 某个 员工拥有的角色和权限列表
     *
     * @param empId 员工id
     * @return
     */
    @GetMapping("/{empId}/roles-permissions")
    @SaCheckRole(type = LoginTypeEnum.EmpType, value = "BranchManager")
    public ResponseEntity<Result<RolesPermissionsVO>> getEmpRolesPermissions(@PathVariable Integer empId) {
        RolesPermissionsVO empRolesPermissions = empService.getEmpRolesPermissions(empId);
        return ResponseEntity.ok(Result.success(empRolesPermissions));
    }

    @PutMapping("/{empId}/roles-permissions")
    @SaCheckRole(type = LoginTypeEnum.EmpType, value = "BranchManager")
    public ResponseEntity<Result<RolesPermissionsVO>> updateEmpRoles(@PathVariable Integer empId, @RequestBody List<Integer> roleIds) {
        empRoleService.updateEmpRoles(empId, roleIds);
        RolesPermissionsVO empRolesPermissions = empService.getEmpRolesPermissions(empId);
        return ResponseEntity.ok(Result.success(empRolesPermissions));
    }


    @GetMapping("/my-roles-permissions")
    public ResponseEntity<Result<RolesPermissionsVO>> getMyRolesPermissions() {
        Integer empId = StpKit.EMP.getLoginIdAsInt();
        RolesPermissionsVO empRolesPermissions = empService.getEmpRolesPermissions(empId);
        return ResponseEntity.ok(Result.success(empRolesPermissions));
    }

    //获取用户列表-带模糊
    @GetMapping("/user-list")
    public ResponseEntity<Result<List<User>>> getUserList(String keyword) {
        List<User> userList = empService.getUserList(keyword);
        return ResponseEntity.ok(Result.success(userList));
    }

    //获取员工列表-带模糊
    @GetMapping("/emp-list")
    public ResponseEntity<Result<List<Emp>>> getEmpList(@RequestParam(required = false) String keyword,
                                                        @RequestParam(required = false) String role) {
        List<Emp> empList = empService.getEmpList(keyword,role);
        return ResponseEntity.ok(Result.success(empList));
    }
}
