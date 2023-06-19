package sk.posam.learning_online;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sk.posam.learning_online.application.CategoryCrudRepository;
import sk.posam.learning_online.application.LanguageCrudRepository;

@SpringBootApplication
public class LearningOnlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningOnlineApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(CategoryCrudRepository categoryRepository, LanguageCrudRepository languageRepository) {


		return args -> {




		};
	}
}