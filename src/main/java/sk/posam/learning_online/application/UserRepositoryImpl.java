package sk.posam.learning_online.application;

import org.springframework.beans.factory.annotation.Autowired;
import sk.posam.learning_online.domain.repositories.UserRepository;

public class UserRepositoryImpl implements UserRepository {
    @Autowired UserCrudRepository userCrudRepository;

}
