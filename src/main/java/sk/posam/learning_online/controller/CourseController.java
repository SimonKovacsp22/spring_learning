package sk.posam.learning_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.learning_online.application.CourseCrudRepository;
import sk.posam.learning_online.application.Impl.CourseRepositoryImpl;
import sk.posam.learning_online.application.Impl.UserRepositoryImpl;
import sk.posam.learning_online.domain.Course;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    CourseCrudRepository courseCrudRepository;

    @Autowired
    CourseRepositoryImpl courseRepositoryImpl;

    @Autowired
    UserRepositoryImpl userRepositoryImpl;

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseCrudRepository.findById(id);
        if(course.isPresent()) {
            return course.get();
        };
        return null;
    }

    @GetMapping("/my/{id}")
    public ResponseEntity<Map<String,Object>> getMyCourses(@PathVariable Long id) {
        Map<String,Object> response = new HashMap<>();
        try {
            response.put("courses", userRepositoryImpl.getMyCourses(id));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
