package renthub;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("renthub.mapper")
@SpringBootApplication
@EnableScheduling //开启定时任务功能
public class RentHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(RentHubApplication.class, args);
    }
}
