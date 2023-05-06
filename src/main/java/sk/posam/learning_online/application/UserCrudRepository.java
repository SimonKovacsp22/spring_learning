package sk.posam.learning_online.application;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sk.posam.learning_online.domain.User;

import java.util.List;
@Repository
public interface UserCrudRepository extends CrudRepository<User,Long> {
    List<User> findByEmail(String email);
}
