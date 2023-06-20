package sk.posam.learning_online.application.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sk.posam.learning_online.domain.Language;
import sk.posam.learning_online.domain.enumeration.LanguageName;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageCrudRepository extends CrudRepository<Language, Long> {
    Optional<Language> findByName(LanguageName name);

    List<Language> findByIdGreaterThan(Long id);
}
