package com.restaubot.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.restaubot.spring.security.CustomerDetailsResponse;
import com.restaubot.spring.security.JwtTokenUtil;

import io.jsonwebtoken.Claims;

public class UserController {
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping()
    public ResponseEntity<?> getCustomerDetails(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        Claims claims = jwtTokenUtil.parseClaims(token);
        int id = (int) claims.get("id");
        String roleUser = claims.get("role").toString().substring(5);
        String typeUser = roleUser.substring(0, 1).toUpperCase() + roleUser.substring(1).toLowerCase();
        CustomerDetailsResponse response = new CustomerDetailsResponse(id, typeUser);
        return ResponseEntity.ok().body(response);
    }
}
