package sk.posam.learning_online.application;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sk.posam.learning_online.domain.User;

import java.util.List;
@Repository
public interface UserCrudRepository extends CrudRepository<User,Long> {
    List<User> findByEmail(String email);

//    @Query("SELECT c FROM Course c JOIN FETCH c.progresses p JOIN c.students cs WHERE p.user = :user AND cs = :user")
//    List<Course> findCoursesByStudent(@Param("user") User user);
}
