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
import sk.posam.learning_online.application.*;
import sk.posam.learning_online.application.Impl.CourseServiceImpl;
import sk.posam.learning_online.application.Impl.LanguageServiceImpl;
import sk.posam.learning_online.application.Impl.UserServiceImpl;
import sk.posam.learning_online.controller.dto.*;
import sk.posam.learning_online.domain.Course;
import sk.posam.learning_online.domain.Section;
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
    SectionCrudRepository sectionCrudRepository;

    @Autowired
    WhatYouWillLearnCrudRepository whatYouWillLearnCrudRepository;

    @Autowired
    LanguageCrudRepository languageCrudRepository;

    @GetMapping("/{id}")
    @JsonView(views.Public.class)
    public Course getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseCrudRepository.findById(id);
        if (course.isPresent()) {
            return course.get();
        }
        ;
        return null;
    }


    @GetMapping("/my/course/{id}")
    @JsonView(views.VideosWithUrl.class)
    public ResponseEntity<Map<String, Object>> getMyCourseById(HttpServletRequest request, @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        String email = userServiceImpl.getEmailFromAuthorizationHeader(request);
        if (email == null) {
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = userCrudRepository.findByEmail(email).orElse(null);
        if (user == null) {
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        LOG.info(String.valueOf(user.getId()));

        try {
            response.put("course", userServiceImpl.getMyCourseById(user.getId(), id));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }


    }

    @GetMapping("/my/{userId}")
    @JsonView(views.VideosWithUrl.class)
    public ResponseEntity<Map<String, Object>> getMyCourses(HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        String email = userServiceImpl.getEmailFromAuthorizationHeader(request);
        if (email == null) {
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = userCrudRepository.findByEmail(email).orElse(null);
        if (user == null) {
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
    public ResponseEntity<Map<String, Object>> getTeacherCourses(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        String email = userServiceImpl.getEmailFromAuthorizationHeader(request);
        if (email == null) {
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = userCrudRepository.findByEmail(email).orElse(null);
        if (user == null) {
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
    public ResponseEntity<Map<String, Object>> getCourseForTeacher(HttpServletRequest request, @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
        if (userId == null) {
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = userCrudRepository.findById(userId).orElse(null);
        if (user == null) {
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            response.put("course", courseServiceImpl.getCourseForTeacher(user.getId(), id));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/languages")
    public ResponseEntity<Map<String, Object>> getAllLanguages() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("languages", languageServiceImpl.getAllLanguages());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/search")
//    @JsonView(views.Public.class)
    public ResponseEntity<Map<String, Object>> getCoursesBySearch
            (@RequestParam("title") String searchTerm,
             @RequestParam(value = "page", defaultValue = "0") int pageNumber,
             @RequestParam(value = "size", defaultValue = "10") int pageSize) {
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Course> coursesPage = courseServiceImpl.searchCoursesByTitle(searchTerm, pageable);
        response.put("courses", coursesPage);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/draft")
    @JsonView(views.VideosWithUrl.class)
    public ResponseEntity<Map<String, Object>> createCourse(@RequestBody CourseRequest courseRequest, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
        LOG.info("userId: " + userId);
        if (userId == null) {
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            response.put("course", courseServiceImpl.createCourseOnClient(courseRequest.getTitle(), courseRequest.getCategoryId(), userId));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/publish/{id}")
    public ResponseEntity<Map<String, Object>> publishCourse( @PathVariable Long id, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Course course = courseCrudRepository.findById(id).orElse(null);
        if (course != null) {
            Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
            if (userId == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userCrudRepository.findById(userId).orElse(null);
            if (user == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (!course.getUser().equals(user)) {
                response.put("message", "This user does not have permission to update this course.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            Course updatedCourse = courseServiceImpl.publishCourse(course);
            if (updatedCourse == null) {
                response.put("message", "The course does not meet the requirements.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            } else {
                response.put("course", updatedCourse);
            }

        }else {
            response.put("message", "This course does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(response);
    }




    @PutMapping(value = "/update/basic/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> UpdateCourseLandingPage(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @PathVariable Long id,
            @RequestParam(value ="title",required = false) String title,
            @RequestParam(value ="subtitle",required = false) String subtitle,
            @RequestParam(value ="description",required = false) String description,
            @RequestParam(value ="categoryId",required = false) Long categoryId,
            @RequestParam(value ="language",required = false) String language,
            HttpServletRequest request) throws IOException {
        Map<String, Object> response = new HashMap<>();
        Course course = courseCrudRepository.findById(id).orElse(null);
        if (course != null) {
            Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
            if (userId == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userCrudRepository.findById(userId).orElse(null);
            if (user == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (!course.getUser().equals(user)) {
                response.put("message", "This user does not have permission to update this course.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String imgUrl = null;

            if (file != null && !file.isEmpty()) {
                Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                imgUrl = (String) uploadedFile.get("secure_url");

            }
            Course updatedCourse = courseServiceImpl.updateCourse(course, title, subtitle, description,language,categoryId, imgUrl);
            if (updatedCourse == null) {
                response.put("message", "Internal serve errror");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            response.put("course", updatedCourse);


        } else {
            response.put("message", "This course does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/price/{id}")
    public ResponseEntity<Map<String, Object>> UpdateCoursePrice(@PathVariable Long id, @RequestBody PriceUpdateRequest priceUpdateRequest, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Course course = courseCrudRepository.findById(id).orElse(null);

        if (course != null) {
            Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
            if (userId == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userCrudRepository.findById(userId).orElse(null);
            if (user == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (!course.getUser().equals(user)) {
                response.put("message", "This user does not have permission to update this course.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            Course updatedCourse = courseServiceImpl.updateCoursePrice(course, priceUpdateRequest.getPrice());
            response.put("course", updatedCourse);

        } else {
            response.put("message", "This course does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/learning/{id}")
    public ResponseEntity<Map<String, Object>> UpdateCoursePrice(@PathVariable Long id, @RequestBody WYWLUpdateRequest wywlUpdateRequest, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Course course = courseCrudRepository.findById(id).orElse(null);
        if (course != null) {
            Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
            if (userId == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userCrudRepository.findById(userId).orElse(null);
            if (user == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (!course.getUser().equals(user)) {
                response.put("message", "This user does not have permission to update this course.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            Course updatedCourse = courseServiceImpl.updateCourseWhatYouWillLearn(course, wywlUpdateRequest.getSentences());
            response.put("course", updatedCourse);
        } else {
            response.put("message", "This course does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/update/curriculum/sections/{id}")
    public ResponseEntity<Map<String, Object>> addSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Course course = courseCrudRepository.findById(id).orElse(null);
        if (course != null) {
            Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
            if (userId == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userCrudRepository.findById(userId).orElse(null);
            if (user == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (!course.getUser().equals(user)) {
                response.put("message", "This user does not have permission to update this course.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            Course updatedCourse = courseServiceImpl.addOrUpdateSection(course, sectionRequest.getTitle(), sectionRequest.getSectionId(), sectionRequest.getRank());
            response.put("course", updatedCourse);

        } else {
            response.put("message", "This course does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/curriculum/sections/{id}")
    public ResponseEntity<Map<String, Object>> deleteSection(@PathVariable Long id, @RequestBody SectionRequest sectionRequest, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Course course = courseCrudRepository.findById(id).orElse(null);
        if (course != null) {
            Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
            if (userId == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userCrudRepository.findById(userId).orElse(null);
            if (user == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (!course.getUser().equals(user)) {
                response.put("message", "This user does not have permission to update this course.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            Course updatedCourse = courseServiceImpl.removeSection(course,sectionRequest.getSectionId());
            response.put("course", updatedCourse);

        } else {
            response.put("message", "This course does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping(value="/update/curriculum/lectures/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> updateLecture(
            @PathVariable Long id,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam(name="title", required = false) String title,
            @RequestParam(name="duration", required = false) Integer duration,
            @RequestParam(name="rank", required = false) Integer rank,
            @RequestParam(name="sectionId") Long sectionId,
            @RequestParam(name="lectureId") Long lectureId,
            HttpServletRequest request) throws IOException {
        Map<String, Object> response = new HashMap<>();
        Course course = courseCrudRepository.findById(id).orElse(null);
        if (course != null) {
            Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
            if (userId == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userCrudRepository.findById(userId).orElse(null);
            if (user == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (!course.getUser().equals(user)) {
                response.put("message", "This user does not have permission to update this course.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            Section section = courseCrudRepository.findSectionByCourseAndId(sectionId,id).orElse(null);
//           Check if course matches the section
            if(section == null) {
                response.put("message", "This section doesn't match the course");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            String sourceUrl = null;
            if(file != null && !file.isEmpty()) {
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "video"));
            sourceUrl = (String) uploadedFile.get("secure_url");
            }
            Section updatedSection = courseServiceImpl
                    .updateLecture(section,
                            lectureId,
                            title,
                            duration,
                            rank,
                            sourceUrl);
            if(updatedSection != null) {
                response.put("section",updatedSection);
            } else {
                response.put("message", "Not all parameters were correct or present");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } else {
            response.put("message", "This course does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(value="/update/curriculum/lectures/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> addLecture(
            @PathVariable Long id,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam(name="title", required = false) String title,
            @RequestParam(name="duration", required = false) Integer duration,
            @RequestParam(name="rank", required = false) Integer rank,
            @RequestParam(name="sectionId") Long sectionId,
            HttpServletRequest request) throws IOException {
        Map<String, Object> response = new HashMap<>();
        Course course = courseCrudRepository.findById(id).orElse(null);
        if (course != null) {
            Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
            if (userId == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userCrudRepository.findById(userId).orElse(null);
            if (user == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (!course.getUser().equals(user)) {
                response.put("message", "This user does not have permission to update this course.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            Section section = courseCrudRepository.findSectionByCourseAndId(sectionId, id).orElse(null);
//           Check if course matches the section
            if (section == null) {
                response.put("message", "This section doesn't match the course");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            String sourceUrl = null;
            if(file != null && !file.isEmpty()) {
                Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "video"));
                sourceUrl = (String) uploadedFile.get("secure_url");
            }
            Section updatedSection = courseServiceImpl
                    .createLecture(section,
                            title,
                            duration,
                            rank,
                            sourceUrl);
            if(updatedSection != null) {
                response.put("section",updatedSection);
            } else {
                response.put("message", "Not all parameters were correct or present");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } else {
            response.put("message", "This course does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/curriculum/lectures/{id}")
    public ResponseEntity<Map<String, Object>> deleteLecture(@PathVariable Long id, @RequestBody LectureRequest lectureRequest, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Course course = courseCrudRepository.findById(id).orElse(null);
        if (course != null) {
            Long userId = userServiceImpl.getIdFromAuthorizationHeader(request);
            if (userId == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userCrudRepository.findById(userId).orElse(null);
            if (user == null) {
                response.put("message", "Token is not present or expired.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (!course.getUser().equals(user)) {
                response.put("message", "This user does not have permission to update this course.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            Section section = courseCrudRepository.findSectionByCourseAndId(lectureRequest.getSectionId(), id).orElse(null);
//           Check if course matches the section
            if (section == null) {
                response.put("message", "This section doesn't match the course");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Section updatedSection = courseServiceImpl.removeLecture(section,lectureRequest.getLectureId());
            if(updatedSection != null) {
                response.put("section",updatedSection);
            } else {
                response.put("message", "Something went wrong");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }


        } else {
            response.put("message", "This course does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

}
