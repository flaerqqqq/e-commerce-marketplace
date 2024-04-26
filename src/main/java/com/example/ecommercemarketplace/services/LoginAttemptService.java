package com.example.ecommercemarketplace.services;

public interface LoginAttemptService {

    void registerFailedLogin(final String ipAddressKey);

    void registerSuccessLogin(final String ipAddressKey);

    boolean isBlocked();
}
