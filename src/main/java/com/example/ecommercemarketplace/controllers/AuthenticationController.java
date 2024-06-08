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

    private final AuthenticationService authService;

    @PostMapping("/login")
    public UserJwtTokenResponseDto login(@RequestBody @Valid UserLoginRequestDto loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponseDto register(@RequestBody @Valid UserRegistrationRequestDto regRequest) {
        return authService.register(regRequest);
    }

    @PostMapping("/refresh")
    @PreAuthorize("permitAll()")
    public UserJwtTokenResponseDto getRefreshJwtToken(HttpServletRequest request) {
        return authService.refresh(request);
    }
}
