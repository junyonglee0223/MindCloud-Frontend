package kr.brain.our_app;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class OurAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(OurAppApplication.class, args);
	}

	@Controller
	public static class HelloController{
		@GetMapping("/")
		public String Hello(){
			//return "Hello World!";
			return "index";
		}
	}
}

