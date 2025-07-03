package renthub;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("renthub.mapper")
@SpringBootApplication
public class RentHubApplication {
	public static void main(String[] args) {
		SpringApplication.run(RentHubApplication.class, args);
	}
}
