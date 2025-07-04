package renthub.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import renthub.domain.dto.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * --- 验证异常处理器 (处理3种常见的验证异常) ---
     */

    /**
     * 1. 处理 @RequestBody 参数校验失败 (POST/PUT请求的JSON参数)
     * 当方法参数使用了 @Validated 或 @Valid，并且校验失败时，会抛出 MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 设置HTTP状态码为400
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 从异常中获取所有字段的错误信息，并用逗号拼接成一个字符串
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("请求参数校验失败: {}", message);
        return Result.error(HttpStatus.BAD_REQUEST.value(), message);
    }

    /**
     * 2. 处理 @RequestParam/@PathVariable 参数校验失败 (GET请求的URL参数)
     * 当方法参数是普通类型（如String, Integer）且校验失败时，或DTO对象未使用@RequestBody时，会抛出 BindException
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("请求参数绑定或校验失败: {}", message);
        return Result.error(HttpStatus.BAD_REQUEST.value(), message);
    }

    /**
     * 3. 处理服务层方法级别校验失败
     * 当在Service层的方法参数上使用验证注解，并且校验失败时，会抛出 ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        log.warn("方法级别校验失败: {}", message);
        return Result.error(HttpStatus.BAD_REQUEST.value(), message);
    }

    /**
     * --- 通用 业务异常处理器 ，
     * 配合业务异常 枚举---
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.warn("业务逻辑异常: code={}, message={}", e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }


    /**
     * 处理所有系统级的、非业务的异常
     */
    @ExceptionHandler(SystemException.class)
    public Result<?> handleSystemException(SystemException e) {
        // 【关键】使用 ERROR 级别记录日志，因为这是需要开发者立即关注的系统问题
        log.error("捕获到系统级异常: {}", e.getMessage(), e); // 必须记录完整的堆栈 e

        // 【关键】对前端隐藏具体的系统错误细节
        return Result.error(e.getCode(), "系统繁忙，请稍后重试");
    }

    /**
     * 处理所有未被捕获的未知异常 (必须要有)
     * 这是最后的防线，保证了即使发生意想不到的错误，服务器也不会崩溃，并且能给前端一个统一的响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 设置HTTP状态码为500
    public Result<Void> handleUnknownException(Exception e) {
        // 在服务器日志中打印完整的错误堆栈，方便排查问题
        log.error("服务器发生未知错误!", e);
        // 出于安全考虑，不要将原始的、详细的错误信息(e.getMessage())暴露给前端用户
        // 只返回一个通用的、友好的错误提示
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器繁忙，请稍后重试");
    }
}
