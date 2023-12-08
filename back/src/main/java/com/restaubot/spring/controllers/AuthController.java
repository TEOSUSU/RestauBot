package com.restaubot.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.restaubot.spring.models.dto.UserDTO;
import com.restaubot.spring.models.entities.UserEntity;
import com.restaubot.spring.security.AuthRequest;
import com.restaubot.spring.security.AuthResponse;
import com.restaubot.spring.security.JwtTokenUtil;

import io.jsonwebtoken.Claims;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenUtil jwtUtil;

    @GetMapping("/getUserInfo")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserInfo(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        Claims claims = jwtUtil.parseClaims(token);
        return UserDTO.builder()
                .idUser((Integer) claims.get("id"))
                .mail(claims.getSubject())
                .role(claims.get("role").toString())
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogin(), request.getPassword()));
            UserEntity user = (UserEntity) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessTokenUser(user);
            AuthResponse response = new AuthResponse(user.getMail(), accessToken);
            return ResponseEntity.ok().body(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
