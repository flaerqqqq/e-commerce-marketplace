package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.*;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    UserJwtTokenResponseDto login(UserLoginRequestDto loginRequest);

    UserRegistrationResponseDto register(UserRegistrationRequestDto registrationRequest);

    UserJwtTokenResponseDto refresh(HttpServletRequest request);
}
