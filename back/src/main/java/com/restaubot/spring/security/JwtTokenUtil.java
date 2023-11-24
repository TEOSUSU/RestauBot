package com.restaubot.spring.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.restaubot.spring.models.entities.CustomerEntity;

import java.util.Date;

//Cette classe permet de créer le JWT token
@Component
public class JwtTokenUtil {
    private static final long EXPIRE_DURATION = (long) 24 * 60 * 60 * 1000;

    @Value("RestauBot20232024tajeb")
    private String SECRET_KEY;

    public String generateAccessToken(CustomerEntity customer) {
        return Jwts.builder()
                .setSubject(String.format("%s", customer.getUsername()))
                .claim("id", customer.getIdCustomer())
                .claim("role", customer.getRole())
                .claim("email", customer.getMail())
                .setIssuer("RestauBot")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // TODO à remplacer par un logger
        }
        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
