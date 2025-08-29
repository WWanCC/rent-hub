package renthub.service.impl;

import renthub.domain.po.Notification;
import renthub.mapper.NotificationMapper;
import renthub.service.INotificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Bai5
 * @since 2025-08-29
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

}
