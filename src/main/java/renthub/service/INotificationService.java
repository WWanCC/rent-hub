package renthub.service;

import renthub.domain.po.Notification;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Bai5
 * @since 2025-08-29
 */
public interface INotificationService extends IService<Notification> {

    /**
     * 将单条通知标记为已读（区分用户和员工）
     * @param id        通知ID
     * @param loginId   当前操作者的登录ID
     * @param loginType 当前操作者的登录类型 ("USER" 或 "EMP")
     * @return 更新后的通知对象
     */
    Notification markAsRead(Integer id, long loginId, String loginType);

    /**
     * 将当前操作者的所有未读通知标记为已读
     * @param loginId   当前操作者的登录ID
     * @param loginType 当前操作者的登录类型 ("USER" 或 "EMP")
     * @return 成功标记的数量
     */
    int markAllAsRead(long loginId, String loginType);
}
