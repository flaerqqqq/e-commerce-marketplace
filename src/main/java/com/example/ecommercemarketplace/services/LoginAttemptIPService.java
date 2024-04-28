package com.example.ecommercemarketplace.services;

public interface LoginAttemptIPService {

    void registerFailedLogin(final String ipAddressKey);

    void registerSuccessLogin(final String ipAddressKey);

    String getClientIP();

    boolean isBlocked();
}
