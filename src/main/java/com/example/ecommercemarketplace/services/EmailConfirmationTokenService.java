package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.models.EmailConfirmationToken;

public interface EmailConfirmationTokenService {

    boolean existsByToken(String token);

    EmailConfirmationToken findByToken(String token);
}
