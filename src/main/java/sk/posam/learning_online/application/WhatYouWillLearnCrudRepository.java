package sk.posam.learning_online.application;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sk.posam.learning_online.domain.WhatYouWillLearn;

import java.util.List;

@Repository
public interface WhatYouWillLearnCrudRepository extends CrudRepository<WhatYouWillLearn,Long> {

    @Query("SELECT w FROM WhatYouWillLearn w WHERE w.course.id = ?1")
    List<WhatYouWillLearn> findByCourseId(Long courseId);
}
