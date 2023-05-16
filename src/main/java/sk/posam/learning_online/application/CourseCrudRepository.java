package sk.posam.learning_online.application;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sk.posam.learning_online.domain.Course;

import java.util.List;
@Repository
public interface CourseCrudRepository extends CrudRepository<Course,Long> {
    @Query("SELECT c FROM Course c JOIN c.categories cat WHERE cat.id = ?1")
    List<Course> findByCategoryId(Long categoryId);
}
