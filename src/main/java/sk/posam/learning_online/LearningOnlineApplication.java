package sk.posam.learning_online;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sk.posam.learning_online.application.CategoryCrudRepository;
import sk.posam.learning_online.application.CourseCrudRepository;
import sk.posam.learning_online.application.UserCrudRepository;

@SpringBootApplication
public class LearningOnlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningOnlineApplication.class, args);


	}
	@Bean
	CommandLineRunner commandLineRunner(UserCrudRepository userCrudRepository, CategoryCrudRepository categoryCrudRepository, CourseCrudRepository courseCrudRepository) {



		return args -> {
//			User michal = new User("Michal","Podstavek","Michal@podstavek.com","12345","admin", LocalDateTime.now());
//			michal.addCourse(new Course("Java","Learning java 17"));
//			Set<Authority> authoritiesSet = new HashSet<>();
//			authoritiesSet.add(new Authority("ROLE_USER"));
//			userCrudRepository.save(michal);

//			Category it = new Category("information technology");
//			Optional<Course> java = courseCrudRepository.findById(752L);
//			if(java.isPresent()) {
//			 Course javaUpdated = java.get();
//			 javaUpdated.addCategory(it);
//
//				courseCrudRepository.save(javaUpdated);
//			}






		};
	}
}