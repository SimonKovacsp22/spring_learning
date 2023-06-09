package sk.posam.learning_online.application.Impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.posam.learning_online.application.CourseCrudRepository;
import sk.posam.learning_online.application.UserCrudRepository;
import sk.posam.learning_online.constants.SecurityConstants;
import sk.posam.learning_online.domain.Course;
import sk.posam.learning_online.domain.User;
import sk.posam.learning_online.domain.repositories.UserRepository;
import sk.posam.learning_online.exceptions.UserNotFoundException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;
@Service
@Transactional
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    UserCrudRepository userCrudRepository;

    @Autowired
    CourseCrudRepository courseCrudRepository;


    @Override
    public Set<Course> getMyCourses(Long userId) {
        User user = userCrudRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User with id: " + userId + " not found");
        }
        Set<Course> courses = user.getCoursesTaken();
        return courses;
    }

    @Override
    public Course getMyCourseById(Long userId, Long courseId) {
        Optional<Course> courseOptional = courseCrudRepository.findCourseByUserIdAndCourseId(userId,courseId);
        return courseOptional.orElse(null);
    }

    @Override
    public String getEmailFromAuthorizationHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String jwtToken = null;
        if (authorizationHeader != null) {
            jwtToken = authorizationHeader.substring(0); // Remove "Bearer " prefix

            SecretKey key = Keys.hmacShaKeyFor(
                    SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
            String email = String.valueOf(claims.get("username"));
            return email;
        } else  return null;
    }

}
