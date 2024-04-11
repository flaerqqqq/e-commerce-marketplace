package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.*;
import jakarta.mail.MessagingException;

public interface AuthenticationService {

    UserJwtTokenResponse login(UserLoginRequest loginRequest);

    UserRegistrationResponse register(UserRegistrationRequest registrationRequest);

    UserJwtTokenResponse refresh(RefreshTokenRequestDto refreshTokenRequestDto);
}
