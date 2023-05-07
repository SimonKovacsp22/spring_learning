package sk.posam.learning_online.application.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import sk.posam.learning_online.application.UserCrudRepository;
import sk.posam.learning_online.domain.repositories.UserRepository;

public class UserRepositoryImpl implements UserRepository {
    @Autowired
    UserCrudRepository userCrudRepository;

}
