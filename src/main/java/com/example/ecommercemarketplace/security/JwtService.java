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

@Service
public class JwtService {

    private final SecretKey key;
    @Value("${jwt.token.expiration.time}")
    private long expirationTime;
    @Value("${jwt.refresh.token.expiration.time}")
    private long refreshTokenExpirationTime;
    @Value("${jwt.password.reset.token.expiration.time}")
    private long passwordResetTokenExpirationTime;

    public JwtService(@Value("${jwt.token.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails userDetails) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + expirationTime);

        return buildToken(userDetails.getUsername(), issuedAt, expiredAt);
    }

    public String generateRefreshToken(String email) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + refreshTokenExpirationTime);

        return buildToken(email, issuedAt, expiredAt);
    }

    public String generatePasswordResetToken(String email) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + passwordResetTokenExpirationTime);

        return buildToken(email, issuedAt, expiredAt);
    }

    private String buildToken(String subject, Date issuedAt, Date expiredAt) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(key)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isValid(String token) {
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

}
