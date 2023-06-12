package sk.posam.learning_online.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sk.posam.learning_online.application.CourseCrudRepository;
import sk.posam.learning_online.application.Impl.CourseServiceImpl;
import sk.posam.learning_online.application.Impl.LanguageServiceImpl;
import sk.posam.learning_online.application.Impl.UserServiceImpl;
import sk.posam.learning_online.application.UserCrudRepository;
import sk.posam.learning_online.application.WhatYouWillLearnCrudRepository;
import sk.posam.learning_online.controller.dto.CourseRequest;
import sk.posam.learning_online.controller.dto.PriceUpdateRequest;
import sk.posam.learning_online.controller.dto.WYWLUpdateRequest;
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
    CourseServiceImpl courseServiceImpl;

    @Autowired
    UserCrudRepository userCrudRepository;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    LanguageServiceImpl languageServiceImpl;

    @Autowired
    WhatYouWillLearnCrudRepository whatYouWillLearnCrudRepository;

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
            String email = userServiceImpl.getEmailFromAuthorizationHeader(request);
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
                response.put("course", userServiceImpl.getMyCourseById(user.getId(),id));
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
        String email = userServiceImpl.getEmailFromAuthorizationHeader(request);
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
            response.put("courses", userServiceImpl.getMyCourses(user.getId()));
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
        String email = userServiceImpl.getEmailFromAuthorizationHeader(request);
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
            response.put("courses", courseServiceImpl.getAllCoursesForTeacher(user.getId()));
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
        Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
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
            response.put("course", courseServiceImpl.getCourseForTeacher(user.getId(),id));
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

    @GetMapping("/search")
//    @JsonView(views.Public.class)
    public ResponseEntity<Map<String,Object>> getCoursesBySearch
            (@RequestParam("title") String searchTerm,
             @RequestParam(value = "page", defaultValue ="0" ) int pageNumber,
             @RequestParam(value = "size", defaultValue ="10" ) int pageSize) {
        Map<String,Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Course> coursesPage = courseServiceImpl.searchCoursesByTitle(searchTerm,pageable);
        response.put("courses",coursesPage);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/draft")
    @JsonView(views.VideosWithUrl.class)
    public ResponseEntity<Map<String,Object>> createCourse(@RequestBody CourseRequest courseRequest,HttpServletRequest request) {
        Map<String,Object> response = new HashMap<>();
        Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
        LOG.info("userId: " + userId);
        if(userId == null) {
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            response.put("course", courseServiceImpl.createCourseOnClient(courseRequest.getTitle(),courseRequest.getCategoryId(),userId));
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



//    @PutMapping(value="/update/{id}", consumes = "multipart/form-data")
//    public ResponseEntity<Map<String,Object>> UpdateCOurse(@RequestParam("file")MultipartFile file, HttpServletRequest request) throws IOException {
//        Map<String,Object> response = new HashMap<>();
//
//        try{
//
//            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "video"));
//
//            response.put("message", "File uploaded successfully");
//            response.put("url",uploadedFile.get("secure_url") );
//        } catch(Exception e) {
//            System.out.println(e);
//
//        }
//        return ResponseEntity.ok(response);
//    }

    @PutMapping(value="/update/basic/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String,Object>> UpdateCourseLandingPage(
            @RequestParam(name = "file",required = false) MultipartFile file,
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("subtitle") String subtitle,
            @RequestParam("description") String description,
            HttpServletRequest request) throws IOException {
        Map<String,Object> response = new HashMap<>();
        Course course = courseCrudRepository.findById(id).orElse(null);
        if(course != null) {
            Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
            if(userId == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userCrudRepository.findById(userId).orElse(null);
            if(user == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if(!course.getUser().equals(user)) {
                response.put("message", "This user does not have permission to update this course.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String imgUrl = null;

            if(file != null) {
                Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                imgUrl = (String) uploadedFile.get("secure_url");

            }
            Course updatedCourse = courseServiceImpl.updateCourse(course,title,subtitle,description,imgUrl);
            if(updatedCourse == null) {
                response.put("message", "Internal serve errror");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            response.put("course", updatedCourse);


        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/price/{id}")
    public ResponseEntity<Map<String,Object>> UpdateCoursePrice(@PathVariable Long id, @RequestBody PriceUpdateRequest priceUpdateRequest, HttpServletRequest request){
        Map<String,Object> response = new HashMap<>();
        Course course = courseCrudRepository.findById(id).orElse(null);
        LOG.info("here");
        if(course != null) {
            Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
            if(userId == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userCrudRepository.findById(userId).orElse(null);
            if(user == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if(!course.getUser().equals(user)) {
                response.put("message", "This user does not have permission to update this course.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            Course updatedCourse = courseServiceImpl.updateCoursePrice(course,priceUpdateRequest.getPrice());
            response.put("course",updatedCourse);

        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/learning/{id}")
    public ResponseEntity<Map<String,Object>> UpdateCoursePrice(@PathVariable Long id, @RequestBody WYWLUpdateRequest wywlUpdateRequest, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Course course = courseCrudRepository.findById(id).orElse(null);
        if(course != null) {
            Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
            if(userId == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userCrudRepository.findById(userId).orElse(null);
            if(user == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if(!course.getUser().equals(user)) {
                response.put("message", "This user does not have permission to update this course.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            Course updatedCourse = courseServiceImpl.updateCourseWhatYouWillLearn(course,wywlUpdateRequest.getSentences());
            response.put("course",updatedCourse);
        }

        return ResponseEntity.ok(response);
    }
}
