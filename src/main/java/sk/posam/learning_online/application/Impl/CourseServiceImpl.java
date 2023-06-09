package sk.posam.learning_online.application.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.posam.learning_online.application.CategoryCrudRepository;
import sk.posam.learning_online.application.CourseCrudRepository;
import sk.posam.learning_online.application.UserCrudRepository;
import sk.posam.learning_online.domain.Category;
import sk.posam.learning_online.domain.Course;
import sk.posam.learning_online.domain.User;
import sk.posam.learning_online.domain.services.CourseService;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseCrudRepository courseCrudRepository;

    @Autowired
    CategoryCrudRepository categoryCrudRepository;

    @Autowired
    UserCrudRepository userCrudRepository;


    @Override
    public Collection<Course> getAllCoursesForCategory(Long categoryId) {
        return courseCrudRepository.findByCategoryId(categoryId);
    }

    @Override
    public Course createCourseOnClient(String title, Long categoryId,Long userId) throws NotFoundException {
        Course newCourse = new Course(title);
        Category category = categoryCrudRepository.findById(categoryId).orElseThrow(NotFoundException::new);
        User user = userCrudRepository.findById(userId).orElseThrow(NotFoundException::new);
        newCourse.setUser(user);
        newCourse.addCategory(category);
        newCourse.setDraft(true);
        return courseCrudRepository.save(newCourse);
    }

    @Override
    public Course getCourseForTeacher(Long userId, Long courseId) throws NotFoundException {
      Course course =  this.courseCrudRepository.findCourseByTeacherIdAndCourseId(userId,courseId).orElseThrow(NotFoundException::new);
      return course;
    }



    public List<Course> getAllCoursesForTeacher(Long userId) {
        return courseCrudRepository.findAllByUserId(userId);
    }


}
