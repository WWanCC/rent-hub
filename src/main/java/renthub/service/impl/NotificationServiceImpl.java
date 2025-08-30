package renthub.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renthub.domain.po.Notification;
import renthub.enums.BusinessExceptionStatusEnum;
import renthub.exception.BusinessException;
import renthub.mapper.NotificationMapper;
import renthub.service.INotificationService;

/**
 * 通知服务实现类
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    @Override
    @Transactional
    public Notification markAsRead(Integer id, long loginId, String loginType) {
        // 1. 根据ID查询通知实体
        Notification notification = this.getById(id);

        // 2. 【核心权限校验】
        // a. 检查通知是否存在
        if (notification == null) {
            throw new BusinessException(BusinessExceptionStatusEnum.ResourceNotFoundException,"通知不存在");
        }

        // b. 检查通知的接收者是否与当前操作者完全匹配
        boolean hasPermission = false;
        if ("USER".equals(loginType) && notification.getUserId() != null && notification.getUserId() == loginId) {
            hasPermission = true;
        } else if ("EMP".equals(loginType) && notification.getEmpId() != null && notification.getEmpId() == loginId) {
            hasPermission = true;
        }

        if (!hasPermission) {
            throw new BusinessException(BusinessExceptionStatusEnum.PERMISSION_NOT_EXIST,"您无权操作该通知");
        }

        // 3. 根据登录类型，更新对应的 is_read 字段
        boolean updated = false;
        if ("USER".equals(loginType) && (notification.getUserIsRead() == null || notification.getUserIsRead() == 0)) {
            notification.setUserIsRead(1);
            updated = true;
        } else if ("EMP".equals(loginType) && (notification.getEmpIsRead() == null || notification.getEmpIsRead() == 0)) {
            notification.setEmpIsRead(1);
            updated = true;
        }

        // 4. 只有在状态确实发生改变时，才执行数据库更新，避免无效的DB操作
        if (updated) {
            this.updateById(notification);
        }

        return notification;
    }

    @Override
    @Transactional
    public int markAllAsRead(long loginId, String loginType) {
        // 使用类型安全的 LambdaUpdateWrapper 进行批量更新
        LambdaUpdateWrapper<Notification> updateWrapper = new LambdaUpdateWrapper<>();

        // 根据登录类型，动态地构建不同的 WHERE 条件和 SET 子句
        if ("USER".equals(loginType)) {
            updateWrapper
                    // WHERE user_id = ?
                    .eq(Notification::getUserId, loginId)
                    // AND user_is_read = 0
                    .eq(Notification::getUserIsRead, 0)
                    // SET user_is_read = 1
                    .set(Notification::getUserIsRead, 1);
        } else if ("EMP".equals(loginType)) {
            updateWrapper
                    // WHERE emp_id = ?
                    .eq(Notification::getEmpId, loginId)
                    // AND emp_is_read = 0
                    .eq(Notification::getEmpIsRead, 0)
                    // SET emp_is_read = 1
                    .set(Notification::getEmpIsRead, 1);
        } else {
            // 如果登录类型未知，不执行任何操作，返回影响行数为 0
            return 0;
        }

        // baseMapper 是 ServiceImpl 自带的，可以直接使用。
        // update 方法会根据 Wrapper 执行批量更新，并返回受影响的行数。
        return baseMapper.update(null, updateWrapper);
    }
}