package renthub.domain.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Result<T> {

    private boolean success;
    private Integer code;
    private String message;
    private T data;

    // 构造函数设为私有，强制通过静态方法创建
    private Result(HttpStatus status, T data) {
        this.code = status.value(); // 直接从枚举获取code
        this.message = status.getReasonPhrase(); // 直接从枚举获取标准消息
        this.data = data;
        this.success = status.is2xxSuccessful(); // 判断是否成功的便捷方法
    }

    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = false; // 自定义错误通常认为是失败
    }

    // --- 成功相关的静态方法 ---
    public static <T> Result<T> success() {
        return new Result<>(HttpStatus.OK, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(HttpStatus.OK, data);
    }

    // --- 失败相关的静态方法 ---
    public static <T> Result<T> error(HttpStatus status) {
        return new Result<>(status, null);
    }

    // 用于返回更具体的业务错误信息
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}

