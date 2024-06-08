package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.models.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String email);

    RefreshToken findByToken(String token);

    boolean existsByToken(String token);

    void removeByToken(String token);

    void deleteExpiredTokens();

}
