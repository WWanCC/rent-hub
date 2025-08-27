package renthub.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessExceptionStatusEnum {
    UnknownException(0, "业务异常"),
    DuplicateResource(1, "数据重复异常"),
    ResourceNotFoundException(2, "数据不存在异常"),
    ResourceNotModifiedException(3, "数据未修改异常"),
    PHONE_ALREADY_REGISTERED(4, "该手机号已被注册"),
    INVALID_CREDENTIALS(40001, "无效的凭证"),
    HOUSE_EXIST(40002, "房屋已存在"),

    ROLE_NOT_EXIST(40003, "角色不存在"),
    PERMISSION_NOT_EXIST(40004, "权限不存在"),
    EMP_NOT_EXIST(40005, "员工不存在"),

    //    合同
    HOUSE_NOT_FOUND(40006, "房源不存在"),
    HOUSE_NOT_AVAILABLE(40007, "该房源已被预定或签约，无法创建合同"),
    CONTRACT_INVALID_STATUS(40008, "合同状态不正确，无法进行操作");

    private final int code;
    private final String description;
}
