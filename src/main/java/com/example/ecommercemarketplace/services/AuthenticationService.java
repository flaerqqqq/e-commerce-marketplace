package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.UserJwtTokenResponse;
import com.example.ecommercemarketplace.dto.UserLoginRequest;
import com.example.ecommercemarketplace.dto.UserRegistrationRequest;
import com.example.ecommercemarketplace.dto.UserRegistrationResponse;

public interface AuthenticationService {

    UserJwtTokenResponse login(UserLoginRequest loginRequest);

    UserRegistrationResponse register(UserRegistrationRequest registrationRequest);
}
