package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@AllArgsConstructor
public class EmailController {

    private EmailService emailService;

    @PostMapping("/send")
    @PreAuthorize("permitAll()")
    public void send(){
        emailService.sendMessageWithVerificationCode("vitaloverzyn32@gmail.com");
    }
}
