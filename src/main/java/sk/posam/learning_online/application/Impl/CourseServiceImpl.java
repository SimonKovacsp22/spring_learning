package sk.posam.learning_online.application.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.posam.learning_online.application.CourseCrudRepository;
import sk.posam.learning_online.domain.Course;
import sk.posam.learning_online.domain.repositories.CourseRepository;

import java.util.Collection;
@Service
@Transactional
public class CourseRepositoryImpl implements CourseRepository {
    @Autowired
    CourseCrudRepository courseCrudRepository;


    @Override
    public Collection<Course> getAllCoursesForCategory(Long categoryId) {
        return courseCrudRepository.findByCategoryId(categoryId);
    }
}
