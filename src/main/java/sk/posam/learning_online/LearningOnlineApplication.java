package sk.posam.learning_online;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sk.posam.learning_online.application.repository.CategoryCrudRepository;
import sk.posam.learning_online.application.repository.CourseCrudRepository;
import sk.posam.learning_online.application.repository.LanguageCrudRepository;
import sk.posam.learning_online.application.repository.UserCrudRepository;
import sk.posam.learning_online.domain.Course;
import sk.posam.learning_online.domain.Rating;
import sk.posam.learning_online.domain.User;

import java.time.LocalDateTime;

@SpringBootApplication
public class LearningOnlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningOnlineApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(CategoryCrudRepository categoryRepository, LanguageCrudRepository languageRepository, UserCrudRepository userCrudRepository,
										CourseCrudRepository courseCrudRepository) {


		return args -> {

			Iterable<User> users = userCrudRepository.findAll();
			users.forEach(System.out::println);
			Iterable<Course> courses = courseCrudRepository.findAll();
			courses.forEach(System.out::println);


			User Tereza = userCrudRepository.findById(652L).orElse(null);
			Course course = courseCrudRepository.findById(1002L).orElse(null);
			if(Tereza != null && course !=null) {
				Rating rating = new Rating();
				rating.setAmount(5);
				rating.setCreatedAt(LocalDateTime.now());
				rating.setMessage("I improved my handicap by 4 points. Love the course.");
				course.addRating(rating);
				Tereza.addRating(rating);
				courseCrudRepository.save(course);
				userCrudRepository.save(Tereza);

			}





		};
	}
}