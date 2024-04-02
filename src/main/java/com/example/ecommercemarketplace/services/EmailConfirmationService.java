package com.example.ecommercemarketplace.services;

public interface EmailConfirmationService {

    void confirm(String token);
}
