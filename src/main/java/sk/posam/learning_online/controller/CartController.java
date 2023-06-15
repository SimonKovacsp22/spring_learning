package sk.posam.learning_online.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.posam.learning_online.application.Impl.CartServiceImpl;
import sk.posam.learning_online.application.Impl.UserServiceImpl;
import sk.posam.learning_online.application.UserCrudRepository;
import sk.posam.learning_online.controller.dto.CartRequest;
import sk.posam.learning_online.domain.Cart;
import sk.posam.learning_online.domain.User;
import sk.posam.learning_online.exceptions.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartServiceImpl cartRepository;
    @Autowired
    UserServiceImpl userRepositoryImpl;

    @Autowired
    UserCrudRepository userCrudRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCartById(@PathVariable Long id) {
        try {
            Cart cart = cartRepository.getActiveCart(id);
            return ResponseEntity.ok(cart);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String,Object>> addToCart(HttpServletRequest request, @RequestBody CartRequest body) {
        Map<String, Object> response = new HashMap<>();
        String email = userRepositoryImpl.getEmailFromAuthorizationHeader(request);
        if(email == null){
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = userCrudRepository.findByEmail(email).orElse(null);
        if(user == null) {
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            cartRepository.addCourseToCart(body.getCourseId(),user.getId());
            response.put("message", "Course added to cart successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to add course to cart");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<Map<String,Object>> removeFromCart(HttpServletRequest request,@RequestBody CartRequest body) {
        Map<String,Object> response = new HashMap<>();
        String email = userRepositoryImpl.getEmailFromAuthorizationHeader(request);
        if(email == null){
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = userCrudRepository.findByEmail(email).orElse(null);
        if(user == null) {
            response.put("message", "Token is not present or expired.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        try {
            cartRepository.removeCourseFromCart(body.getCourseId(),user.getId());
            response.put("message", "Course removed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to remove course from cart");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
