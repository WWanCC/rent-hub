package renthub.auth;

import cn.dev33.satoken.fun.SaFunction;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，定义详细认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {
            SaFunction userAuth = StpKit.USER::checkLogin;
            SaFunction empAuth = StpKit.EMP::checkLogin;

            //无需登录 白名单
            SaRouter.match(
                    "/user/register",
                    "/user/login",
                    "/regions",
                    "/admin/login"
            ).stop();
            SaRouter.match("/houses/**").matchMethod("GET").stop();
            SaRouter.match("/houses/search").matchMethod("GET", "POST").stop();

            // ===================  C端用户专属接口认证 ===================
//            SaRouter.match("/user/**").check(userAuth);
            SaRouter.match("/**/user/**").check(userAuth);

            // ===================  B端员工专属接口认证 ===================
//            SaRouter.match("/admin/**").check(empAuth);
            SaRouter.match("/**/admin/**").check(empAuth);

        })).addPathPatterns("/**");//拦截全部请求,进入sa-token路由拦截器，更细粒度鉴权

    }
}
