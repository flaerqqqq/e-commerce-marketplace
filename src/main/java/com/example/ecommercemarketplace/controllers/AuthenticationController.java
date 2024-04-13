package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.UserJwtTokenResponseDto;
import com.example.ecommercemarketplace.dto.UserLoginRequestDto;
import com.example.ecommercemarketplace.dto.UserRegistrationRequestDto;
import com.example.ecommercemarketplace.dto.UserRegistrationResponseDto;
import com.example.ecommercemarketplace.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public UserJwtTokenResponseDto login(@RequestBody @Valid UserLoginRequestDto loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponseDto register(@RequestBody @Valid UserRegistrationRequestDto registrationRequest) {
        return authenticationService.register(registrationRequest);
    }

    @PostMapping("/refresh")
    @PreAuthorize("permitAll()")
    public UserJwtTokenResponseDto refreshToken(HttpServletRequest request) {
        return authenticationService.refresh(request);
    }

}
