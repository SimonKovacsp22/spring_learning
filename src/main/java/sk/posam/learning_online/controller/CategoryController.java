package sk.posam.learning_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.learning_online.application.Impl.CategoryRepositoryImpl;
import sk.posam.learning_online.application.Impl.CourseRepositoryImpl;
import sk.posam.learning_online.domain.Category;
import sk.posam.learning_online.domain.Course;

import java.util.Collection;
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepositoryImpl categoryRepositoryImpl;
    @Autowired
    CourseRepositoryImpl courseRepositoryImpl;



    @GetMapping()
    public Collection<Category> getAllCategories() {
        return categoryRepositoryImpl.getAllCategories();
    }

    @GetMapping("/{id}")
    public Collection<Course> getAllCoursesForCategory(@PathVariable("id") Long id) {
        return courseRepositoryImpl.getAllCoursesForCategory(id);
    }
}
