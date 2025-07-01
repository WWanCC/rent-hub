package renthub.exception;


import lombok.Getter;

@Getter
public class SystemException extends RuntimeException {

    // 默认的错误码，例如 500
    private static final int DEFAULT_CODE = 500;

    private final int code;

    public SystemException(String message) {
        super(message);
        this.code = DEFAULT_CODE;
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
        this.code = DEFAULT_CODE;
    }

}

