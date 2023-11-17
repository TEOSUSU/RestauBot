package com.restaubot.spring.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.restaubot.spring.models.entities.CustomerEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
    
    private static final long EXPIRE_DURATION = 24L * 60 * 60 * 1000;

    @Value("${app.jwt.secret}")
    private String secretKey;

    public String generateAccessToken(CustomerEntity customer) {
        return Jwts.builder()
                .setSubject(String.format("%s", customer.getMail()))
                .setIssuer("RestauBot").setIssuedAt(new Date())
                .claim("role", customer.getRole())
                .claim("id", customer.getIdCustomer())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

}
