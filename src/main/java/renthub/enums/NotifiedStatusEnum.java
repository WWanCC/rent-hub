package renthub.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 通用的“是否已通知”状态枚举
 */
@Getter
@RequiredArgsConstructor
public enum NotifiedStatusEnum {

    /**
     * 未通知 (数据库中存 0)
     */
    UNNOTIFIED(0, "未通知"),

    /**
     * 已通知 (数据库中存 1)
     */
    NOTIFIED(1, "已通知");

    @EnumValue
    private final int code;
    private final String description;
}