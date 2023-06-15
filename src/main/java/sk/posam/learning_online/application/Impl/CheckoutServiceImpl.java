package sk.posam.learning_online.application.Impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.posam.learning_online.application.CourseCrudRepository;
import sk.posam.learning_online.application.UserCrudRepository;
import sk.posam.learning_online.controller.dto.PaymentInfo;
import sk.posam.learning_online.controller.dto.Purchase;
import sk.posam.learning_online.domain.*;
import sk.posam.learning_online.domain.services.CheckoutService;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    CheckoutServiceImpl(@Value("${stripe.key.secret}") String secretKey)  {
        Stripe.apiKey = secretKey;
    }

    @Autowired
    UserCrudRepository userCrudRepository;

    @Autowired
    CourseCrudRepository courseCrudRepository;
    @Autowired
    CartServiceImpl cartRepositoryImpl;

    @Override
    @Transactional
    public boolean placeOrder(Purchase purchase) {
        String email = purchase.getEmail();
        User customer = userCrudRepository.findByEmail(email).orElse(null);
        if(customer != null) {
//      Add Order to user

            Order order = new Order();
            order.setCreatedAt(LocalDateTime.now());
            order.setStatus("resolved");
            customer.addOrder(order);

//      Add orderItems to order

            Set<OrderItem> orderItems = purchase.getOrderItems();
            orderItems.forEach(order::addOrderItem);

//      Add courses to user

            orderItems.forEach(orderItem -> {
                Course course = courseCrudRepository.findById(orderItem.getCourseId())
                        .orElseThrow(()->new EntityNotFoundException("Course with id: " + orderItem.getCourseId() + " not found!"));

                course.addStudent(customer);
                order.addPrice(course.getPrice());

            });
//      Set cart inactive
            Cart activeCart = cartRepositoryImpl.getActiveCart(customer.getId());
            Set<Course> coursesInCart = activeCart.getCourses();
            if(coursesInCart.isEmpty()) {
                throw (new RuntimeException("There are no courses in cart."));
            }
            activeCart.setActive(false);
            userCrudRepository.save(customer);
            return true;
        } else {
            throw (new EntityNotFoundException("User with email: " + email + " not found!"));
        }

    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        float amount  = 0;

        for (OrderItem item: paymentInfo.getOrderItems()
             ) {
            Course course = courseCrudRepository.findById(item.getCourseId()).
                    orElseThrow(()->new EntityNotFoundException("Course with id: " + item.getCourseId() + " not found!"));
            amount += course.getPrice() * 100;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("amount", Math.round(amount));
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description","Lurnx Inc. purchase.");
        params.put("receipt_email", paymentInfo.getReceiptEmail());

        return PaymentIntent.create(params);
    }
}
