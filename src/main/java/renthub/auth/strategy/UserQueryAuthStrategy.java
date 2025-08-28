package renthub.auth.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;
import renthub.auth.StpKit;
import renthub.domain.po.User;

/**
 * 【用户】的查询鉴权策略：基于角色的访问控制。
 * 只有管理员才能查询用户信息列表。
 */
@Component
public class UserQueryAuthStrategy implements QueryAuthStrategy<User> {

    /**
     * 【亮明身份】
     * 明确声明：本策略专门负责处理对 User 实体的查询。
     */
    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    /**
     * 【执行安检】
     * 核心逻辑：检查当前登录的账号是否为“EMP”类型，并且是否拥有“admin”角色。
     *
     * @param wrapper 需要被修改的 QueryWrapper
     */
    @Override
    public void applyAuth(QueryWrapper<User> wrapper) {
        // 1. 前置检查：调用此接口的人必须是“EMP”类型，并且拥有“admin”角色。
        //    StpKit.EMP.checkRole("admin") 会自动完成这两步校验。
        //    如果校验失败，会直接抛出异常，中断请求。
        StpKit.EMP.checkRole("admin");

        // 2. 对于管理员，我们允许他查询所有用户，所以不需要添加额外的 WHERE 条件。
        System.out.println("Executing UserQueryAuthStrategy: Access granted for admin role.");
    }
}