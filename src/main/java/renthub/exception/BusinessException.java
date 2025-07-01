package renthub.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import renthub.enums.BusinessExceptionStatusEnum;


@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    //备用构造
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(BusinessExceptionStatusEnum statusEnum) {
        // 直接从枚举中获取 message 和 code，并调用父类构造器
        super(statusEnum.getDescription());
        this.code = statusEnum.getCode();
    }

    // 支持动态消息 例如： "用户'张三'已存在
    public BusinessException(BusinessExceptionStatusEnum statusEnum, String dynamicDetail) {
        // 将枚举的描述作为模板，动态填充信息
        super(String.format("%s: %s", statusEnum.getDescription(), dynamicDetail));
        this.code = statusEnum.getCode();
    }

}
