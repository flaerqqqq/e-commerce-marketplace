package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.*;
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
    public UserJwtTokenResponse login(@RequestBody @Valid UserLoginRequest loginRequest){
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponse register(@RequestBody @Valid UserRegistrationRequest registrationRequest){
        return authenticationService.register(registrationRequest);
    }

    @PostMapping("/refresh")
    @PreAuthorize("permitAll()")
    public UserJwtTokenResponse refreshToken(HttpServletRequest request){
        return authenticationService.refresh(request);
    }

}
