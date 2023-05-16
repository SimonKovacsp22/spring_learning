package sk.posam.learning_online.application.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.posam.learning_online.application.UserCrudRepository;
import sk.posam.learning_online.domain.Course;
import sk.posam.learning_online.domain.User;
import sk.posam.learning_online.domain.repositories.UserRepository;
import sk.posam.learning_online.exceptions.UserNotFoundException;

import java.util.Set;
@Service
@Transactional
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    UserCrudRepository userCrudRepository;


    @Override
    public Set<Course> getMyCourses(Long userId) {
        User user = userCrudRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User with id: " + userId + " not found");
        }
        Set<Course> courses = user.getCoursesTaken();
        return courses;
    }
}
