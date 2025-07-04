package renthub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * 密码加密器配置
 */
public class PasswordEncoderConfig {
    /**
     * 定义一个 PasswordEncoder 的 Bean。
     * Spring 会将这个 BCryptPasswordEncoder 实例注册到 Spring IoC 容器中。
     * 其他组件需要 PasswordEncoder 时，直接通过 @Autowired 注入即可。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
