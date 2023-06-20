package sk.posam.learning_online.application.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.posam.learning_online.application.repository.LanguageCrudRepository;
import sk.posam.learning_online.domain.Language;
import sk.posam.learning_online.domain.services.LanguageService;

import java.util.List;
@Service
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    LanguageCrudRepository languageCrudRepository;
    @Override
    public List<Language> getAllLanguages() {
        return (List<Language>) languageCrudRepository.findAll();
    }
}
