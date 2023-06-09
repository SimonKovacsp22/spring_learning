package sk.posam.learning_online.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.learning_online.application.Impl.CheckoutServiceImpl;
import sk.posam.learning_online.controller.dto.PaymentInfo;
import sk.posam.learning_online.controller.dto.Purchase;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    private CheckoutServiceImpl checkoutServiceImpl;

    @PostMapping("/purchase")
    public ResponseEntity<Map<String,Object>> placeOrder(@RequestBody Purchase purchase) {
        Map<String,Object> response = new HashMap<>();
        boolean success = false;

        try{
           success = checkoutServiceImpl.placeOrder(purchase);
           response.put("success",success);
           return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Error err){
            response.put("error", err.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @PostMapping()
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfo paymentInfo) throws StripeException {
        PaymentIntent paymentIntent = checkoutServiceImpl.createPaymentIntent(paymentInfo);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<>(paymentStr, HttpStatus.OK);
    }



}
