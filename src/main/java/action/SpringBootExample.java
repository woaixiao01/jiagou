package action;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 我的第一个SpringBoot案例，太神奇了！
 * @author zhongzhenhao
 * 用main方法来跑动tomcat，而且全部东西都是已经内置好了，太方便了！！！
 */


@RestController
@EnableAutoConfiguration
@ComponentScan("scheduled")
@EnableScheduling
public class SpringBootExample {
	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}
	
	public static void main(String[] args) {		
		SpringApplication.run(SpringBootExample.class, args);		
	}

}