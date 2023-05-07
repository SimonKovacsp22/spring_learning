package sk.posam.learning_online.application;

import org.springframework.data.repository.CrudRepository;
import sk.posam.learning_online.domain.Course;

public interface CourseCrudRepository extends CrudRepository<Course,Long> {
}
