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
        String rolePerson = claims.get("role").toString().substring(5);
        String typePerson = rolePerson.substring(0, 1).toUpperCase() + rolePerson.substring(1).toLowerCase();
        //System.out.println(id);
        //System.out.println(rolePerson);
        //System.out.println(typePerson);
        CustomerDetailsResponse response = new CustomerDetailsResponse(id, typePerson);
        return ResponseEntity.ok().body(response);
    }
}
