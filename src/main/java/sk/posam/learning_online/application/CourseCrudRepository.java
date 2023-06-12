package sk.posam.learning_online.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.posam.learning_online.domain.Course;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseCrudRepository extends JpaRepository<Course,Long> {
    @Query("SELECT c FROM Course c JOIN c.categories cat WHERE cat.id = ?1 AND c.draft = false")
    List<Course> findByCategoryId(Long categoryId);

    @Query("SELECT c FROM Course c JOIN c.students s JOIN User u ON s.id = u.id WHERE u.id = :userId AND c.id = :courseId")
    Optional<Course> findCourseByUserIdAndCourseId(Long userId, Long courseId);

    @Query("SELECT c FROM Course c JOIN FETCH c.user u WHERE u.id= :userId AND c.id = :courseId")
    Optional<Course> findCourseByTeacherIdAndCourseId(Long userId, Long courseId);

    List<Course> findAllByUserId(Long userId);

    Page<Course> findByTitleIgnoreCaseContainingAndDraftIsFalse(String title, Pageable pageable);

}
