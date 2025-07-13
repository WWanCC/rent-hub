package renthub.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BusinessExceptionStatusEnum {
    UnknownException(0, "业务异常"),
    DuplicateResource(1, "数据重复异常"),
    ResourceNotFoundException(2, "数据不存在异常"),
    ResourceNotModifiedException(3, "数据未修改异常"),
    PHONE_ALREADY_REGISTERED(4, "该手机号已被注册"),
    INVALID_CREDENTIALS(40001, "无效的凭证"),
    HOUSE_EXIST(40002, "房屋已存在"),

    ROLE_NOT_EXIST(40003, "角色不存在");

    private final int code;
    private final String description;
}
