package com.restaubot.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.restaubot.spring.models.dto.ResponseCustomerInfo;
import com.restaubot.spring.models.entities.CustomerEntity;
import com.restaubot.spring.security.AuthRequest;
import com.restaubot.spring.security.AuthResponse;
import com.restaubot.spring.security.JwtTokenUtil;
import com.restaubot.spring.services.CustomerService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenUtil jwtUtil;
    @Autowired
    CustomerService customerService;

    @PostMapping("/customer/login")
    public ResponseEntity<Object> loginCustomer(@RequestBody AuthRequest request) {
        try {
            org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogin(), request.getPassword()));
            CustomerEntity customer = (CustomerEntity) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(customer);
            AuthResponse response = new AuthResponse(customer.getMail(), accessToken);
            return ResponseEntity.ok().body(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /*@PostMapping("/restaurant/login")
    public ResponseEntity<Object> loginRestaurant(@RequestBody AuthRequest request) {
        try {
            org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogin(), request.getPassword()));
            RestaurantEntity restaurant = (RestaurantEntity) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(restaurant);
            AuthResponse response = new AuthResponse(restaurant.getMail(), accessToken);
            return ResponseEntity.ok().body(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }*/

    @GetMapping("/getCustomerInfo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCustomerInfo getCustomerInfo(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        Claims claims = jwtUtil.parseClaims(token);
        if ((claims.get("role").toString()).equals("ROLE_CUSTOMER")) {
            return ResponseCustomerInfo.builder()
                    .customerId((Integer) claims.get("id"))
                    .mail(claims.getSubject())
                    .role(claims.get("role").toString())
                    .build();
        } else {
            return ResponseCustomerInfo.builder()
                    .customerId((Integer) claims.get("id"))
                    .mail(claims.getSubject())
                    .role(claims.get("role").toString())
                    .build();
        }
    }

    /*@GetMapping("/getRestaurantInfo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCustomerInfo getRestaurantInfo(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        Claims claims = jwtUtil.parseClaims(token);
        if ((claims.get("role").toString()).equals("ROLE_RESTAURANT")) {
            return ResponseCustomerInfo.builder()
                    .restaurantId((Integer) claims.get("id"))
                    .mail(claims.getSubject())
                    .role(claims.get("role").toString())
                    .build();
        } else {
            return ResponseCustomerInfo.builder()
                    .restaurantId((Integer) claims.get("id"))
                    .mail(claims.getSubject())
                    .role(claims.get("role").toString())
                    .build();
        }
    }*/
}
