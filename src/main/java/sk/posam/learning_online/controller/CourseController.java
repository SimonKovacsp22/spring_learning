package sk.posam.learning_online.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.posam.learning_online.application.CourseCrudRepository;
import sk.posam.learning_online.application.Impl.CourseServiceImpl;
import sk.posam.learning_online.application.Impl.LanguageServiceImpl;
import sk.posam.learning_online.application.Impl.UserServiceImpl;
import sk.posam.learning_online.application.UserCrudRepository;
import sk.posam.learning_online.controller.dto.CourseRequest;
import sk.posam.learning_online.domain.Course;
import sk.posam.learning_online.domain.User;
import sk.posam.learning_online.domain.views.views;
import sk.posam.learning_online.filter.AuthoritiesLoggingAtFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final Logger LOG =
            Logger.getLogger(AuthoritiesLoggingAtFilter.class.getName());

    private Cloudinary cloudinary;
     CourseController() {
        Map config = new HashMap();
        config.put("cloud_name", "kalgaro");
        config.put("api_key", "933828242339141");
        config.put("api_secret", "zpOCyFxnb4mjDzfPQM7cj4a0Qj8");

        this.cloudinary = new Cloudinary(config);
    }
    @Autowired
    CourseCrudRepository courseCrudRepository;

    @Autowired
    CourseServiceImpl courseRepositoryImpl;

    @Autowired
    UserCrudRepository userCrudRepository;

    @Autowired
    UserServiceImpl userRepositoryImpl;

    @Autowired
    LanguageServiceImpl languageServiceImpl;

    @GetMapping("/{id}")
    @JsonView(views.Public.class)
    public Course getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseCrudRepository.findById(id);
        if(course.isPresent()) {
            return course.get();
        };
        return null;
    }



    @GetMapping("/my/course/{id}")
    @JsonView(views.VideosWithUrl.class)
    public ResponseEntity<Map<String,Object>> getMyCourseById(HttpServletRequest request,@PathVariable Long id) {
        Map<String,Object> response = new HashMap<>();
            String email = userRepositoryImpl.getEmailFromAuthorizationHeader(request);
            if(email == null){
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userCrudRepository.findByEmail(email).orElse(null);
            if(user == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            LOG.info(String.valueOf(user.getId()));

            try {
                response.put("course", userRepositoryImpl.getMyCourseById(user.getId(),id));
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }


    }

    @GetMapping("/my/{userId}")
    @JsonView(views.VideosWithUrl.class)
    public ResponseEntity<Map<String,Object>> getMyCourses(HttpServletRequest request) {

        Map<String,Object> response = new HashMap<>();
        String email = userRepositoryImpl.getEmailFromAuthorizationHeader(request);
        if(email == null){
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = userCrudRepository.findByEmail(email).orElse(null);
        if(user == null) {
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            response.put("courses", userRepositoryImpl.getMyCourses(user.getId()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/teach")
    @JsonView(views.VideosWithUrl.class)
    public ResponseEntity<Map<String,Object>> getTeacherCourses(HttpServletRequest request) {
        Map<String,Object> response = new HashMap<>();
        String email = userRepositoryImpl.getEmailFromAuthorizationHeader(request);
        if(email == null){
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = userCrudRepository.findByEmail(email).orElse(null);
        if(user == null) {
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            response.put("courses", courseRepositoryImpl.getAllCoursesForTeacher(user.getId()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/teach/course/{id}")
    @JsonView(views.VideosWithUrl.class)
    public ResponseEntity<Map<String,Object>> getCourseForTeacher(HttpServletRequest request,@PathVariable Long id) {
        Map<String,Object> response = new HashMap<>();
        Long userId = userRepositoryImpl.getIdFromAuthorizationHeader(request);
        if(userId == null){
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = userCrudRepository.findById(userId).orElse(null);
        if(user == null) {
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            response.put("course", courseRepositoryImpl.getCourseForTeacher(user.getId(),id));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/languages")
    public ResponseEntity<Map<String,Object>> getAllLanguages() {
        Map<String,Object> response = new HashMap<>();
        try {
            response.put("languages",languageServiceImpl.getAllLanguages());
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/draft")
    @JsonView(views.VideosWithUrl.class)
    public ResponseEntity<Map<String,Object>> createCourse(@RequestBody CourseRequest courseRequest,HttpServletRequest request) {
        Map<String,Object> response = new HashMap<>();
        Long userId = userRepositoryImpl.getIdFromAuthorizationHeader(request);
        LOG.info("userId: " + userId);
        if(userId == null) {
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            response.put("course", courseRepositoryImpl.createCourseOnClient(courseRequest.getTitle(),courseRequest.getCategoryId(),userId));
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @PutMapping(value="/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String,Object>> UpdateCOurse(@RequestParam("file")MultipartFile file, HttpServletRequest request) throws IOException {
        Map<String,Object> response = new HashMap<>();

        try{

            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "video"));

        response.put("message", "File uploaded successfully");
        response.put("url",uploadedFile.get("secure_url") );
        } catch(Exception e) {
            System.out.println(e);

        }
        return ResponseEntity.ok(response);
    }


}
