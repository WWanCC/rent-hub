package renthub.service.impl;

import renthub.domain.po.Emp;
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
public class EmpServiceImpl extends ServiceImpl<EmpMapper, Emp> implements EmpService {

}
