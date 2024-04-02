package com.example.ecommercemarketplace.services;

public interface EmailConfirmationTokenService {

    boolean existsByToken(String token);

}
