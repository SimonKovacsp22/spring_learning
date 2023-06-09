package sk.posam.learning_online.application.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.posam.learning_online.application.CategoryCrudRepository;
import sk.posam.learning_online.domain.Category;
import sk.posam.learning_online.domain.repositories.CategoryRepository;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
@Service
@Transactional
public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    CategoryCrudRepository categoryCrudRepository;


    @Override
    public Collection<Category> getAllCategories() {
        return StreamSupport.stream(categoryCrudRepository.findAll().spliterator(), false).collect(Collectors.toList()) ;
    }
}
