package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.UserJwtTokenResponse;
import com.example.ecommercemarketplace.dto.UserLoginRequest;
import com.example.ecommercemarketplace.dto.UserRegistrationRequest;
import com.example.ecommercemarketplace.dto.UserRegistrationResponse;
import com.example.ecommercemarketplace.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public UserJwtTokenResponse login(@RequestBody UserLoginRequest loginRequest){
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserRegistrationResponse register(@RequestBody UserRegistrationRequest registrationRequest){
        return authenticationService.register(registrationRequest);
    }
}
