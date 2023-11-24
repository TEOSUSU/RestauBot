package com.restaubot.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.restaubot.spring.models.entities.CustomerEntity;
import com.restaubot.spring.security.AuthRequest;
import com.restaubot.spring.security.AuthResponse;
import com.restaubot.spring.security.JwtTokenUtil;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenUtil jwtUtil;

    //Cette methode permet d'authentifier la personne et de lui envoyer son JWT
    @PostMapping("/login/customer")
    public ResponseEntity<?> loginCustomer(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogin(), request.getPassword())
            );
            CustomerEntity customer = (CustomerEntity) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(customer);
            //String roleCustomer = customer.getPermission().substring(5);
           //String userType = roleCustomer.substring(0, 1).toUpperCase() + roleCustomer.substring(1).toLowerCase();
            AuthResponse response = new AuthResponse(customer.getUsername(), accessToken);// userType);
            return ResponseEntity.ok().body(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
