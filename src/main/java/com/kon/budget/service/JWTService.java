package com.kon.budget.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    private static final int MILLISECOUNDS_IN_DAY = 86_400_000;
    private static final String SECRET = "mySecret";

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }
    //wyciaga username z tokena
    public String extractUserName(String token) {
        var claims = extractClaims(token);
        return claims.getSubject();
    }

    //wyciaga date wygascniecia tokenuy
    public Date extractionExpirationDate(String token) {
        var claims = extractClaims(token);
        return claims.getExpiration();
    }

    //sprawdza czy token jest jeszcze wazny
    public Boolean validateToken(String token, UserDetails userDetails) {
        var userName = extractUserName(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractionExpirationDate(token).before(new Date());
    }

    public String generateJWTToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", userDetails.getUsername());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + MILLISECOUNDS_IN_DAY))
                .addClaims(claims) // dodakowe informacie o uzytkowiku
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();

    }
}
