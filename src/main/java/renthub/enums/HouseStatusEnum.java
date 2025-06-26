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
     * 2: 已签约
     * 房源已签订租约，暂时不可租赁。
     */
    RENTED(2, "已签约");

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
