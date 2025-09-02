//package renthub.aspect;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//
//@Aspect
//@Slf4j
//@Component
//public class LogAspect0 {
//
//    /**
//     * 定义一个切点 (Pointcut)
//     * - execution(* renthub.service..*.*(..)) : 匹配 renthub.service 包及其所有子包下的所有类的所有方法。
//     * - execution(* renthub.controller..*.*(..)) : 匹配 renthub.controller 包及其所有子包下的所有类的所有方法。
//     * - "||" 表示“或”逻辑。
//     */
//    @Pointcut("execution(* renthub.service..*.*(..)) || execution(* renthub.controller..*.*(..))")
//    public void logPointcut() {
//    }
//
//
//    /**
//     * 定义一个环绕通知 (Around Advice)，并使用上面定义的切点
//     *
//     * @param joinPoint 包含了被拦截方法的所有信息（方法名、参数、所属类等）
//     * @return 目标方法的返回值
//     * @throws Throwable 目标方法可能抛出的异常
//     */
//    @Around("logPointcut()")
//    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        // --- 方法执行前 ---
//        long startTime = System.currentTimeMillis();
//
//        // 获取方法签名，从中可以得到方法名、类名等信息
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        String className = joinPoint.getTarget().getClass().getName();
//        String methodName = signature.getName();
//
//        // 获取方法参数
//        Object[] args = joinPoint.getArgs();
//
//        log.info("======> Executing: {}.{}({})",
//                className,
//                methodName,
//                Arrays.toString(args));
//
//        // --- 执行目标方法 ---
//        Object result;
//        try {
//            // 真正地去调用Service或Controller里的方法
//            result = joinPoint.proceed();
//        } catch (Throwable e) {
//            // --- 方法抛出异常 ---
//            long endTime = System.currentTimeMillis();
//            long executionTime = endTime - startTime;
//
//            log.error("<====== Exception in: {}.{}(), Execution time: {}ms, Exception: {}",
//                    className,
//                    methodName,
//                    executionTime,
//                    e.getMessage());
//
//            // 必须重新抛出异常，否则业务层的异常就会被“吞掉”
//            throw e;
//        }
//
//        // --- 方法成功返回后 ---
//        long endTime = System.currentTimeMillis();
//        long executionTime = endTime - startTime;
//
//        log.info("<====== 调用: 【{}.{}()】，Execution time: {}ms\n返回值: {}\n",
//                className,
//                methodName,
//                executionTime,
//                result); // 打印返回值
//
//        return result;
//    }
//}
