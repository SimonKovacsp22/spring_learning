package sk.posam.learning_online.domain.repositories;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Repository;
import sk.posam.learning_online.domain.Course;

import java.util.Set;

@Repository
public interface UserRepository {

 Set<Course> getMyCourses (Long userId);

 Course getMyCourseById(Long userId, Long courseId);

 String getEmailFromAuthorizationHeader(HttpServletRequest request);

}
