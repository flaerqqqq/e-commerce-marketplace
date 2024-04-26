package com.example.ecommercemarketplace.services;

public interface LoginAttemptEmailService {
    
    void registerSuccessfulLogin(String email);
    
    void registerFailureLogin(String email);
    
    boolean isBlocked(String email);

    void unblockUserLogin();

    String calculateTimeToUnblock(String email);
}
