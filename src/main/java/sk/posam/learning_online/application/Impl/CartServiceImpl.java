package sk.posam.learning_online.application.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.posam.learning_online.application.CartCrudRepository;
import sk.posam.learning_online.application.CourseCrudRepository;
import sk.posam.learning_online.application.UserCrudRepository;
import sk.posam.learning_online.domain.Cart;
import sk.posam.learning_online.domain.Course;
import sk.posam.learning_online.domain.User;
import sk.posam.learning_online.domain.repositories.CartRepository;
import sk.posam.learning_online.exceptions.UserNotFoundException;
@Service
@Transactional
public class CartRepositoryImpl implements CartRepository {
    @Autowired
    CartCrudRepository cartCrudRepository;

    @Autowired
    UserCrudRepository userCrudRepository;

    @Autowired
    CourseCrudRepository courseCrudRepository;


    @Override
    public Cart getActiveCart(Long userId) {
            User user = userCrudRepository.findById(userId).orElse(null);
            if (user == null) {
                throw new UserNotFoundException("User with id: " + userId + " not found");
            }
        Cart activeCart = cartCrudRepository.findFirstByActiveTrueAndUser_Id(userId);
        if(activeCart == null) {
                activeCart = createActiveCart(user);
        }

        return activeCart;
    }
    @Override
    public Cart createActiveCart(User user) {
        Cart activeCart = new Cart();
        activeCart.setActive(true);
        activeCart.setUser(user);
        return cartCrudRepository.save(activeCart);
    }

    @Override
    public void addCourseToCart(Long courseId,Long userId) {
        Course course = courseCrudRepository.findById(courseId).orElse(null);
        User user = userCrudRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User with id: " + userId + " not found");
        }
        if(course == null) {
            throw new UserNotFoundException("Course with id: " + courseId + " not found");
        }

        Cart cart = getActiveCart(userId);
        cart.addCourse(course);



    }

    @Override
    public void removeCourseFromCart(Long courseId,Long userId) {
        Course course = courseCrudRepository.findById(courseId).orElse(null);
        User user = userCrudRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User with id: " + userId + " not found");
        }
        if(course == null) {
            throw new UserNotFoundException("Course with id: " + courseId + " not found");
        }

        Cart cart = getActiveCart(userId);
        cart.removeCourse(course);
    }


}
