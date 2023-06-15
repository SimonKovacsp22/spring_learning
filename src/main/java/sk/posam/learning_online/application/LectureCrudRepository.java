package sk.posam.learning_online.application;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sk.posam.learning_online.domain.Lecture;

@Repository
public interface LectureCrudRepository extends CrudRepository<Lecture,Long> {
}
