package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    UserJwtTokenResponse login(UserLoginRequest loginRequest);

    UserRegistrationResponse register(UserRegistrationRequest registrationRequest);

    UserJwtTokenResponse refresh(HttpServletRequest request);
}
