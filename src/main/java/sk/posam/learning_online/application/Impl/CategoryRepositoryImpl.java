package sk.posam.learning_online.application.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import sk.posam.learning_online.application.CategoryCrudRepository;
import sk.posam.learning_online.domain.Category;
import sk.posam.learning_online.domain.repositories.CategoryRepository;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    CategoryCrudRepository categoryCrudRepository;


    @Override
    public Collection<Category> getAllCategories() {
        return StreamSupport.stream(categoryCrudRepository.findAll().spliterator(), false).collect(Collectors.toList()) ;
    }
}
