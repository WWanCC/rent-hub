package renthub.auth.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;
import renthub.domain.po.House;

/**
 * 【房源】的查询鉴权策略：公开访问。
 * <p>
 * 实现了 QueryAuthStrategy 接口，并被 @Component 注解标记，
 * Spring 会自动扫描并将其作为一个 Bean 注入到我们的策略中心。
 */
@Component
public class HouseQueryAuthStrategy implements QueryAuthStrategy<House> {

    /**
     * 【亮明身份】
     * 明确声明：本策略专门负责处理对 House 实体的查询。
     */
    @Override
    public Class<House> getEntityClass() {
        return House.class;
    }

    /**
     * 【执行安检】
     * 核心逻辑：因为房源是公开可查询的，所以我们的“安检”流程就是“直接放行”。
     * 我们不对 QueryWrapper 做任何修改。
     *
     * @param wrapper 需要被修改的 QueryWrapper
     */
    @Override
    public void applyAuth(QueryWrapper<House> wrapper) {
        // 无操作 (No-Op)。
        // 这明确地表达了“此资源的查询是公开的，无需任何权限过滤”的业务规则。
        System.out.println("Executing HouseQueryAuthStrategy: Public access, no auth filter applied.");
        System.out.println("此资源的查询是公开的，无需任何权限过滤”的业务规则");
    }
}