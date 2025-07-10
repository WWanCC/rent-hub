package renthub.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import renthub.auth.StpKit;
import renthub.domain.dto.EmpLoginDTO;
import renthub.domain.po.Emp;
import renthub.enums.BusinessExceptionStatusEnum;
import renthub.exception.BusinessException;
import renthub.mapper.EmpMapper;
import renthub.service.EmpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台员工 服务实现类
 * </p>
 *
 * @author Bai5
 * @since 2025-06-25
 */
@Service
@RequiredArgsConstructor
public class EmpServiceImpl extends ServiceImpl<EmpMapper, Emp> implements EmpService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public SaTokenInfo login(EmpLoginDTO empLoginDTO) {
        LambdaQueryWrapper<Emp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Emp::getUsername, empLoginDTO.getUsername());
        Emp emp = this.getOne(wrapper);

        if (emp == null || !passwordEncoder.matches(empLoginDTO.getPassword(), emp.getPassword())) {
            throw new BusinessException(BusinessExceptionStatusEnum.INVALID_CREDENTIALS, "用户名或密码错误");
        }

        StpKit.EMP.login(emp.getId(), new SaLoginParameter().setExtra("username", emp.getUsername()));
        return StpKit.EMP.getTokenInfo();
    }
}
