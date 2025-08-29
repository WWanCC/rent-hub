package renthub.auth.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;
import renthub.auth.StpKit;
import renthub.domain.po.Emp;

/**
 * 【员工】的查询鉴权策略：严格的管理员角色访问控制。
 */
@Component // <-- 必须有这个注解，Spring 才能自动发现并注入
public class AdminQueryAuthStrategy implements QueryAuthStrategy<Emp> {

    @Override
    public Class<Emp> getEntityClass() {
        return Emp.class;
    }

    @Override
    public void applyAuth(QueryWrapper<Emp> wrapper) {
        StpKit.EMP.checkLogin();
    }
}