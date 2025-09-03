package renthub.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import java.io.IOException;

@Configuration
@Profile("dev-embedded")
public class EmbeddedRedisConfig {

    private RedisServer redisServer; // 将 server 提升为成员变量

    // ★★★ 使用 @PostConstruct 注解来构造和启动 ★★★
    @PostConstruct
    public void startRedis() throws IOException {
        // 使用构造函数来创建实例
        this.redisServer = new RedisServer(6379);
        // 手动启动
        this.redisServer.start();
    }

    // ★★★ 使用 @PreDestroy 注解来停止 ★★★
    @PreDestroy
    public void stopRedis() throws IOException {
        if (this.redisServer != null) {
            this.redisServer.stop();
        }
    }


}