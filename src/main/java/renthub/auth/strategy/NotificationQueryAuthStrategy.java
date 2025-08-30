package renthub.auth.strategy;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;
import renthub.auth.StpKit;
import renthub.domain.po.Notification;

@Component
public class NotificationQueryAuthStrategy implements QueryAuthStrategy<Notification> {
    @Override
    public Class<Notification> getEntityClass() {
        return Notification.class;
    }

    @Override
    public void applyAuth(QueryWrapper<Notification> wrapper) {
        // 1. 【核心】检查当前是否有任何账号登录
//        StpUtil.checkLogin();

        // 2. 判断当前登录的是 USER 还是 EMP
        if (StpKit.USER.isLogin()) {
            // 如果是用户登录，添加用户过滤条件
            wrapper.eq("user_id", StpKit.USER.getLoginIdAsInt());
        }
        else if (StpKit.EMP.isLogin()) {
            // 如果是员工登录，添加员工过滤条件
            wrapper.eq("emp_id", StpKit.EMP.getLoginIdAsInt());
        }
        else {
            // 理论上 checkLogin() 已经覆盖了此情况，但作为防御性编程
            throw new SecurityException("无法识别的登录类型");
        }
    }
}
