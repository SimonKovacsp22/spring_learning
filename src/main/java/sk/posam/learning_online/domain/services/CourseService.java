package sk.posam.learning_online.domain.services;

import org.springframework.data.crossstore.ChangeSetPersister;
import sk.posam.learning_online.domain.Course;
import sk.posam.learning_online.domain.Language;

import java.util.Collection;
import java.util.List;

public interface CourseService {
    Collection<Course> getAllCoursesForCategory(Long categoryId);

    Course createCourseOnClient(String title,Long categoryId,Long userId) throws ChangeSetPersister.NotFoundException;

    Course getCourseForTeacher(Long userId,Long courseId) throws ChangeSetPersister.NotFoundException;





}
