package renthub.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Objects;

@Aspect
@Slf4j
@Component
public class LogAspect {

    // =================  1. 定义分离的切点  =================

    /**
     * 为 renthub.controller 包及其子包下的所有类的所有方法定义一个切点
     */
    @Pointcut("execution(* renthub.controller..*.*(..))")
    public void controllerPointcut() {
    }

    /**
     * 为 renthub.service 包及其子包下的所有类的所有方法定义一个切点
     */
    @Pointcut("execution(* renthub.service..*.*(..))")
    public void servicePointcut() {
    }


    // =================  2. 为 Controller 创建专属的环绕通知  =================

    @Around("controllerPointcut()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // Spring AOP 默认不直接提供对 HttpServletRequest 的访问，需要通过 RequestContextHolder 获取
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getSimpleName(); // 使用 SimpleName 更简洁
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        // 打印更丰富的 HTTP 请求信息
        log.info("======> 【Controller】 Request received: {} {} from IP: {}\n",
                request.getMethod(),
                request.getRequestURL().toString(),
                request.getRemoteAddr());
        log.info("======> 【Controller】 Executing: {}.{}({})\n",
                className,
                methodName,
                Arrays.toString(args));

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("<====== [Controller] Exception in: {}.{}(), Execution time: {}ms, Exception: {}",
                    className, methodName, executionTime, e.getMessage());
            throw e;
        }

        long executionTime = System.currentTimeMillis() - startTime;
        log.info("<====== [Controller] Finished: {}.{}(), Execution time: {}ms",
                className, methodName, executionTime);
        // Controller 的返回值通常比较复杂，且包含敏感信息，可以考虑不打印完整的 result
        // 如果需要，可以像下面这样打印，但要注意数据大小和敏感性
        // log.info("<====== [Controller] Return Value: {}", result);

        return result;
    }


    // =================  3. 为 Service 创建专属的环绕通知  =================

    @Around("servicePointcut()")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();

        log.info("======> 【Service】 Executing: {}.{}({})\n",
                className,
                methodName,
                Arrays.toString(args));

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("<====== [Service] Exception in: {}.{}(), Execution time: {}ms, Exception: {}\n",
                    className, methodName, executionTime, e.getMessage());
            throw e;
        }

        long executionTime = System.currentTimeMillis() - startTime;
        log.info("<====== [Service] Finished: {}.{}(), Execution time: {}ms",
                className, methodName, executionTime);
        // Service层的返回值通常也可能很大（比如一个大的列表），可以选择不打印或只打印摘要
        // log.info("<====== [Service] Return Value: {}", result);

        return result;
    }
}