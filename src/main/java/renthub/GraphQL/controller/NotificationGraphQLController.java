package renthub.GraphQL.controller;

import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import renthub.auth.StpKit;
import renthub.domain.po.Notification;
import renthub.service.INotificationService;

/**
 * 专门处理 Notification 相关 GraphQL 查询与变更的控制器。
 */
@Controller
@RequiredArgsConstructor
public class NotificationGraphQLController {

    // ... 未来这里会注入 INotificationService 用于 Mutation ...
    private final INotificationService notificationService;

    /**
     * 【核心】为 Notification 类型下的 "isRead" 字段提供自定义解析逻辑。
     * <p>
     * 这个方法会在 GraphQL 引擎解析 Notification 对象的每个字段时被调用。
     * 它的职责是：根据当前登录用户的身份，动态地决定 isRead 字段的值。
     *
     * @param notification 上一层 DataFetcher (即 CommonQueryService) 返回的 Notification 实体对象。
     * @return Boolean - 动态计算出的“已读”状态。
     */
    @SchemaMapping(typeName = "Notification", field = "isRead")
    public boolean getIsReadForNotification(Notification notification) {
        // 1. 检查当前是否有任何账号登录
//        if (!StpUtil.isLogin()) {
//            return false; // 对于未登录的请求，我们认为所有通知都是“未读”
//        }

        // 2. 判断当前登录的是 USER 还是 EMP
        if (StpKit.USER.isLogin()) {
            // 如果是【用户】登录，我们就去读取数据库中的 user_is_read 字段
            // 并将数据库的 int (0/1) 转换为 boolean (true/false)
            return notification.getUserIsRead() != null && notification.getUserIsRead() == 1;
        }

        if (StpKit.EMP.isLogin()) {
            // 如果是【员工】登录，我们就去读取数据库中的 emp_is_read 字段
            return notification.getEmpIsRead() != null && notification.getEmpIsRead() == 1;
        }

        // 对于其他未知情况，安全起见，返回 false
        return false;
    }

    // --- 【新增】处理写操作的 Mutation ---

    @MutationMapping // 对应 Schema 中的 "markNotificationAsRead"
    public Notification markNotificationAsRead(@Argument Integer id) {
        if (StpKit.USER.isLogin()) {
            return notificationService.markAsRead(id, StpKit.USER.getLoginIdAsLong(), "USER");
        }
        else if (StpKit.EMP.isLogin()) {
            return notificationService.markAsRead(id, StpKit.EMP.getLoginIdAsLong(), "EMP");
        }
        else {
            // 如果两个域都没有登录，抛出未登录异常
            StpUtil.checkLogin(); // 这会抛出 NotLoginException
            return null; // 理论上不会执行到这里
        }
    }

    @MutationMapping // 对应 Schema 中的 "markAllNotificationsAsRead"
    public int markAllNotificationsAsRead() {
        if (StpKit.USER.isLogin()) {
            return notificationService.markAllAsRead(StpKit.USER.getLoginIdAsLong(), "USER");
        }
        else if (StpKit.EMP.isLogin()) {
            return notificationService.markAllAsRead(StpKit.EMP.getLoginIdAsLong(), "EMP");
        }
        else {
            StpUtil.checkLogin();
            return 0;
        }
    }
}

