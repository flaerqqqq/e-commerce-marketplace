package com.example.ecommercemarketplace.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.token.expiration.time}")
    private long expirationTime;

    private SecretKey key;

    public JwtService(@Value("${jwt.token.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails userDetails){

        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(key)
                .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token){
        return extractAllClaims(token).getSubject();
    }

    public boolean isValid(String token){
        try {
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(key)
                                .build()
                                .parseClaimsJws(token)
                                .getBody();

            return !claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
                 | IllegalArgumentException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public String generatePasswordResetToken(String email) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime()  + 1800000);

        return Jwts.builder()
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .setSubject(email)
                .signWith(key)
                .compact();
    }
}
