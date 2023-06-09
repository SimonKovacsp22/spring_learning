package sk.posam.learning_online.domain.repositories;

import sk.posam.learning_online.domain.Cart;
import sk.posam.learning_online.domain.User;

public interface CartRepository {
    Cart getActiveCart(Long userId);

    Cart createActiveCart(User user);

    void addCourseToCart(Long courseId, Long userId);

    void removeCourseFromCart(Long courseId, Long userId);



}
