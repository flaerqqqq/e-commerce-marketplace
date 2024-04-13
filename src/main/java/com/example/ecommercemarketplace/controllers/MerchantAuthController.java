package com.example.ecommercemarketplace.controllers;

import com.example.ecommercemarketplace.dto.MerchantRegistrationRequestDto;
import com.example.ecommercemarketplace.dto.MerchantRegistrationResponseDto;
import com.example.ecommercemarketplace.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/merchants")
@AllArgsConstructor
public class MerchantAuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MerchantRegistrationResponseDto register(@RequestBody MerchantRegistrationRequestDto registrationRequestDto) {
        return authenticationService.registerMerchant(registrationRequestDto);
    }
}
