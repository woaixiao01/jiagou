package scheduled;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * 我的第一个Schedule的Spring Boot案例，但是问题是，这两个类只能是在同个包下才能够识别。
 * Spring Boot配置起来要比Spring简单，但是要注意的地方也是蛮多的。
 * @author zhongzhenhao
 * @date 2017-08-04
 */
@SpringBootApplication
@ComponentScan("scheduled")
@EnableScheduling
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class);
    }
}