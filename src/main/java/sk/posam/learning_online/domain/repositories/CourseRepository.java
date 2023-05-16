package sk.posam.learning_online.domain.repositories;

import sk.posam.learning_online.domain.Course;

import java.util.Collection;

public interface CourseRepository {
    Collection<Course> getAllCoursesForCategory(Long categoryId);

}
