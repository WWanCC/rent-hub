package renthub.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 合同状态枚举
 * 用于统一管理合同的生命周期状态
 */
@Getter
@RequiredArgsConstructor
public enum ContractStatusEnum {

    PENDING_CONFIRMATION(1, "待用户确认"),
    IN_PROGRESS(2, "进行中"), //租期中
    FINISHED(3, "已结束"), //到租期或之后
    VOIDED(4, "已作废"); // 例如用户拒绝或超时未确认

    /**
     * Mp注解
     * 标记存入数据库的值。
     * 在进行数据库操作时，会读取此注解标记的字段值。
     */
    @EnumValue
    private final int code;

    /**
     * 枚举的描述信息，用于业务代码中的展示或日志。
     */
    private final String description;
}
