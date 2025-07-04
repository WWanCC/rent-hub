package renthub.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginTypeEnum {
    USER("user", "C端用户"),
    EMP("emp", "B端后台员工");

    private final String code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static LoginTypeEnum getByCode(String code) {
        for (LoginTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
