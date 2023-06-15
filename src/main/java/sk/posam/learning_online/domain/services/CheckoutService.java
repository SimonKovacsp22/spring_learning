package sk.posam.learning_online.domain.services;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import sk.posam.learning_online.controller.dto.PaymentInfo;
import sk.posam.learning_online.controller.dto.Purchase;

public interface CheckoutService {
    boolean placeOrder(Purchase purchase);
    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
