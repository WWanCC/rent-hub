package renthub.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BusinessExceptionStatusEnum {
    UnknownException(0, "业务异常"),
    DuplicateResource(1, "数据重复异常"),
    ResourceNotFoundException(2, "数据不存在异常");

    private final int code;
    private final String description;
}
