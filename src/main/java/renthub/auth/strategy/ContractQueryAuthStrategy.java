package renthub.auth.strategy;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;
import renthub.auth.StpKit;
import renthub.domain.po.RentalContract;

/**
 * 【合同】的查询鉴权策略：行级别安全。
 * 用户只能查询到属于自己的合同。
 */
@Component
public class ContractQueryAuthStrategy implements QueryAuthStrategy<RentalContract> {

    /**
     * 【亮明身份】
     * 明确声明：本策略专门负责处理对 RentalContract 实体的查询。
     */
    @Override
    public Class<RentalContract> getEntityClass() {
        return RentalContract.class;
    }

    /**
     * 【执行安检】
     * 核心逻辑：
     * 1. 检查用户是否已登录。
     * 2. 获取当前登录用户的ID。
     * 3. 强制在查询条件中加入 "user_id = [当前用户ID]"。
     *
     * @param wrapper 需要被修改的 QueryWrapper
     */
    @Override
    public void applyAuth(QueryWrapper<RentalContract> wrapper) {
        // 1. 前置检查：首先，调用此接口的人必须是已登录的“USER”类型用户。
        //    如果未登录，StpKit.USER.checkLogin() 会直接抛出异常，中断请求，非常安全。
        StpKit.USER.checkLogin();

        // 2. 获取当前登录的用户ID
        int currentUserId = StpKit.USER.getLoginIdAsInt();

        // 3. 应用行级别过滤条件
        //    这行代码会在最终的 SQL WHERE 子句中，追加 "AND user_id = ?"
        wrapper.eq("user_id", currentUserId);

        System.out.println("Executing ContractQueryAuthStrategy: Applied filter for userId = " + currentUserId);
    }
}