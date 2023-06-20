package sk.posam.learning_online.application.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sk.posam.learning_online.domain.Cart;
@Repository
public interface CartCrudRepository extends CrudRepository<Cart,Long> {
    Cart findFirstByActiveTrueAndUser_Id(Long userId);
}
