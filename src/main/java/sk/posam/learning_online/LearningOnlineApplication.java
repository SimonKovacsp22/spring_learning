package sk.posam.learning_online;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sk.posam.learning_online.application.UserCrudRepository;

@SpringBootApplication
public class LearningOnlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningOnlineApplication.class, args);


	}
	@Bean
	CommandLineRunner commandLineRunner(UserCrudRepository userCrudRepository) {



		return args -> {
//			User michal = new User("Michal","Podstavek","Michal@podstavek.com","12345","admin", LocalDateTime.now());
//			michal.addCourse(new Course("Java","Learning java 17"));
//			Set<Authority> authoritiesSet = new HashSet<>();
//			authoritiesSet.add(new Authority("ROLE_USER"));
//			authoritiesSet.add(new Authority("ROLE_ADMIN"));
//			userCrudRepository.save(michal);


		};
	}
}