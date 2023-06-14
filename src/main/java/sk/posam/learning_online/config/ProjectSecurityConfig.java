package sk.posam.learning_online.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sk.posam.learning_online.application.UserCrudRepository;
import sk.posam.learning_online.filter.CsrfCookieFilter;
import sk.posam.learning_online.filter.JWTGenerationFilter;
import sk.posam.learning_online.filter.JWTValidationFilter;
import sk.posam.learning_online.filter.RequestValidationBeforeFilter;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
public class ProjectSecurityConfig {

    private final UserCrudRepository userCrudRepository;

    public ProjectSecurityConfig(UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    @Bean
    public JWTGenerationFilter jwtGenerationFilter() {
        return new JWTGenerationFilter(userCrudRepository);
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .cors().configurationSource(corsConfigurationSource())

                .and().csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers(
                        "/categories","/register","/courses","/cart","/cart/add","/cart/remove","/courses/my",
                                "/courses/my/course","/checkout","/checkout/purchase","/courses/teach","/courses/draft","/courses/update/basic/**",
                                "/courses/teach/course/**","/courses/languages","/courses/search", "/courses/update/price/**",
                                "/courses/update/learning/**","/courses/update/curriculum/sections/**","/courses/delete/curriculum/sections/**")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(jwtGenerationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTValidationFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers(antMatcher(HttpMethod.POST, "/cart/add")).hasRole("USER")
                .requestMatchers(antMatcher(HttpMethod.POST, "/cart/remove")).hasRole("USER")
                .requestMatchers(antMatcher(HttpMethod.POST,"/courses/draft")).hasRole("USER")
                .requestMatchers(antMatcher(HttpMethod.POST,"/courses/update/curriculum/sections/**")).hasRole("USER")
                .requestMatchers(antMatcher(HttpMethod.GET,"/cart/**")).hasRole("USER")
                .requestMatchers(antMatcher(HttpMethod.PUT, "/courses/update/basic/**")).hasRole("USER")
                .requestMatchers(antMatcher(HttpMethod.PUT, "/courses/update/price/**")).hasRole("USER")
                .requestMatchers(antMatcher(HttpMethod.PUT,"/courses/update/learning/**")).permitAll()
                .requestMatchers(antMatcher(HttpMethod.GET,"/courses/my/**")).hasRole("USER")
                .requestMatchers(antMatcher(HttpMethod.GET,"/courses/my/course/**")).hasRole("USER")
                .requestMatchers(antMatcher(HttpMethod.GET,"/courses/teach")).hasRole("USER")
                .requestMatchers(antMatcher(HttpMethod.GET,"/courses/teach/course/**")).hasRole("USER")
                .requestMatchers(antMatcher(HttpMethod.DELETE,"/courses/delete/curriculum/sections/**")).hasRole("USER")
                .requestMatchers("/user").authenticated()
                .requestMatchers("/register").permitAll()
                .requestMatchers(antMatcher(HttpMethod.POST,"/courses/search")).permitAll()
                .requestMatchers(antMatcher(HttpMethod.POST,"/checkout/purchase")).permitAll()
                .requestMatchers(antMatcher(HttpMethod.POST,"/checkout")).permitAll()
                .requestMatchers(antMatcher(HttpMethod.GET,"/courses/languages")).permitAll()
                .requestMatchers(antMatcher(HttpMethod.GET,"/categories/**")).permitAll()
                .requestMatchers(antMatcher(HttpMethod.GET,"/courses/**")).permitAll()
                .and().formLogin()
                .and().httpBasic();
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setExposedHeaders(Arrays.asList("Authorization"));
        config.setMaxAge(36000L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
