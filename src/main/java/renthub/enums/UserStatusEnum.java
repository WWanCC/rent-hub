package renthub.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatusEnum {
    DELETED(0, "已注销"),
    NORMAL(1, "正常");

    @EnumValue
    private final int code;
    private final String description;
}
