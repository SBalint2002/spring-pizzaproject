package hu.pizzavalto.pizzaproject;

import hu.pizzavalto.pizzaproject.init.StarterDataInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public StarterDataInitializer dataInitializer() {
		return new StarterDataInitializer();
	}
}
