package action;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class SpringBootExample {
	@RequestMapping("/")
	String home() {
		return "Hello Docker World!";
	}
	
	public static void main(String[] args) {		
		SpringApplication.run(SpringBootExample.class, args);		
	}

}