package baseProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "file:/usr/local/hitv/springboot.cfg")
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
