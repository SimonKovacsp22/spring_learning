package sk.posam.learning_online.application;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sk.posam.learning_online.domain.Category;

@Repository
public interface CategoryCrudRepository extends CrudRepository<Category,Long> {


}
