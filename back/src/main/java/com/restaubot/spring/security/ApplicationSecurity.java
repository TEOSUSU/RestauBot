package com.restaubot.spring.security;

import java.util.Optional;

import com.restaubot.spring.models.entities.CustomerEntity;
import com.restaubot.spring.models.entities.RestaurantEntity;
import com.restaubot.spring.repositories.CustomerRepository;
import com.restaubot.spring.repositories.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;

@Configuration
public class ApplicationSecurity {
    
    @Autowired
    private JwtTokenFilter jwtTokenFilter;


    //Permet d'accepter les requetes envoyées par la svelteApp
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*");
            }
            
        };
    }

    //Permet de trouver la personne connectée dans la base de donnée
    @Bean
    public UserDetailsService userDetailsService(CustomerRepository customerRepository,
                                                  RestaurantRepository restaurantRepository) {
        return username -> {
            Optional<CustomerEntity> customer = customerRepository.findByMail(username);
            if (customer.isPresent()) {
                return customer.get();
            }

            Optional<RestaurantEntity> restaurant = restaurantRepository.findByMail(username);
            if (restaurant.isPresent()) {
                return restaurant.get();
            }

            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    //Permet de configurer la sécurité de l'api, ainsi que les urls accéssible par certain role
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/auth/login/**").permitAll()
                .antMatchers("/auth/getUserInfo").permitAll()
                .antMatchers("/api/categories/").permitAll()
                .antMatchers("/api/types/").permitAll()
                .antMatchers("/api/dishes").permitAll()
                .antMatchers("/api/menus").permitAll()
                .antMatchers("/api/restaurant/id/**").permitAll()
                .anyRequest().authenticated();

        http
                .rememberMe()
                .rememberMeCookieName("monCookie")
                .tokenValiditySeconds(86400);

        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                ex.getMessage()
                        )
                );
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
