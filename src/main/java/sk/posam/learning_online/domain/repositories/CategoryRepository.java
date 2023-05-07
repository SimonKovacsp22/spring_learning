package sk.posam.learning_online.domain.repositories;

import sk.posam.learning_online.domain.Category;

import java.util.Collection;

public interface CategoryRepository {

    Collection<Category> getAllCategories();
}
