package sk.posam.learning_online.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.learning_online.application.Impl.CategoryServiceImpl;
import sk.posam.learning_online.application.Impl.CourseServiceImpl;
import sk.posam.learning_online.domain.Category;
import sk.posam.learning_online.domain.Course;
import sk.posam.learning_online.domain.views.views;

import java.util.Collection;
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryRepositoryImpl;
    @Autowired
    CourseServiceImpl courseRepositoryImpl;



    @GetMapping()
    public Collection<Category> getAllCategories() {
        return categoryRepositoryImpl.getAllCategories();
    }

    @GetMapping("/{id}")
    @JsonView(views.Public.class)
    public Collection<Course> getAllCoursesForCategory(@PathVariable("id") Long id) {
        return courseRepositoryImpl.getAllCoursesForCategory(id);
    }
}
