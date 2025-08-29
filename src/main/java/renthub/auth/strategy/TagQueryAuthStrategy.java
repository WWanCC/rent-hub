package renthub.auth.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;
import renthub.auth.StpKit;
import renthub.domain.po.Emp;
import renthub.domain.po.Tag;

/**
 * 【员工】的查询鉴权策略：严格的管理员角色访问控制。
 */
@Component // <-- 必须有这个注解，Spring 才能自动发现并注入
public class TagQueryAuthStrategy implements QueryAuthStrategy<Tag> {

    @Override
    public Class<Tag> getEntityClass() {
        return Tag.class;
    }

    @Override
    public void applyAuth(QueryWrapper<Tag> wrapper) {
        // 标签信息是公开的，无需任何权限过滤
    }
}