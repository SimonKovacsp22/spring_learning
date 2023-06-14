package sk.posam.learning_online.domain.services;

import org.springframework.data.crossstore.ChangeSetPersister;
import sk.posam.learning_online.domain.Course;

import java.util.ArrayList;
import java.util.Collection;

public interface CourseService {
    Collection<Course> getAllCoursesForCategory(Long categoryId);

    Course createCourseOnClient(String title,Long categoryId,Long userId) throws ChangeSetPersister.NotFoundException;

    Course getCourseForTeacher(Long userId,Long courseId) throws ChangeSetPersister.NotFoundException;

    Course updateCourse(Course course, String title, String subtitle, String description, String imgUrl);

    Course updateCoursePrice(Course course, Double price);

    Course updateCourseWhatYouWillLearn(Course course, ArrayList<String> sentences);

    Course addOrUpdateSection(Course course,String title, Long sectionId,Integer rank);

    Course removeSection(Course course,Long sectionId);




}
