package renthub.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HouseStatusEnum {

    /**
     * 0: 已下架
     * 房源信息仅保留，不对外展示，不可租赁。
     */
    DELISTED(0, "已下架"),

    /**
     * 1: 待租
     * 房源已发布，处于可被租赁的状态。
     */
    AVAILABLE(1, "待租"),

    /**
     * 2: 等待签约确认
     * 等待签约确认，用户端应不可见，计划使用定时器到期释放
     */
    LOCKED(2, "等待签约确认"),

    /**
     * 3: 已签约
     * 房源已签订租约，暂时不可租赁。
     */
    RENTED(3, "已签约");

    /**Mp注解
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
