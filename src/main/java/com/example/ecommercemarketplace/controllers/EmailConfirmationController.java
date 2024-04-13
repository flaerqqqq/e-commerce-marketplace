package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.services.EmailConfirmationService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/confirm")
@AllArgsConstructor
public class EmailConfirmationController {

    private final EmailConfirmationService emailConfirmationService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public String confirm(@RequestParam String token) {
        emailConfirmationService.confirm(token);
        return "You can close this !";
    }
}
