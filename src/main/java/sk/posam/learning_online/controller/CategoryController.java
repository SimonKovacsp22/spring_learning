package sk.posam.learning_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import sk.posam.learning_online.application.Impl.CategoryRepositoryImpl;
import sk.posam.learning_online.domain.Category;

import java.util.Collection;

public class CategoryController {

    @Autowired
    private CategoryRepositoryImpl categoryRepositoryImpl;

    @GetMapping("/categories")
    public Collection<Category> getAllCategories() {
        return categoryRepositoryImpl.getAllCategories();
    }
}
