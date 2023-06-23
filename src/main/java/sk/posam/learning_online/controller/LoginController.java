package sk.posam.learning_online.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.learning_online.application.repository.UserCrudRepository;
import sk.posam.learning_online.controller.dto.UserDto;
import sk.posam.learning_online.domain.Authority;
import sk.posam.learning_online.domain.User;
import sk.posam.learning_online.filter.AuthoritiesLoggingAtFilter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@RestController
public class LoginController {
    private final Logger LOG =
            Logger.getLogger(AuthoritiesLoggingAtFilter.class.getName());

    @Autowired
    private UserCrudRepository userCrudRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        User savedUser = null;
        ResponseEntity response = null;
        try {
            String hashPwd = passwordEncoder.encode(user.getPwd());
            user.setPwd(hashPwd);
            user.setCreateDt(LocalDateTime.now());

            user.setRole("user");
            Set<Authority> authoritiesSet = new HashSet<>();
			authoritiesSet.add(new Authority("ROLE_USER"));
            user.setAuthorities(authoritiesSet);
            savedUser = userCrudRepository.save(user);
            if (savedUser.getId() > 0) {
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(savedUser);
            } else {
                response = ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to register user due to an unknown error")

                ;
            }
        }
        catch (DataIntegrityViolationException e) {
            response = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email address already exists");}
        catch (Exception ex) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occured due to " + ex.getMessage());
        }
        return response;
    }

    @RequestMapping("/user")
    public ResponseEntity<Map<String, Object>> getUserDetailsAfterLogin(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        User user = userCrudRepository.findByEmail(authentication.getName()).orElse(null);
        LOG.info("here");
        if (user != null) {
            response.put("user",new UserDto(user));
            return ResponseEntity.ok(response);
        } else {
            response.put("error","Wrong credentials. User not found.");
            return ResponseEntity.ok(response);
        }

    }

}
