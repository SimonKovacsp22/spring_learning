package sk.posam.learning_online.domain.services;

import sk.posam.learning_online.domain.Cart;
import sk.posam.learning_online.domain.User;

public interface CartService {
    Cart getActiveCart(Long userId);

    Cart createActiveCart(User user);

    void addCourseToCart(Long courseId, Long userId);

    void removeCourseFromCart(Long courseId, Long userId);



}
