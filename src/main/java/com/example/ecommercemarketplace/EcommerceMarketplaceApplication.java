package com.example.ecommercemarketplace;

import com.example.ecommercemarketplace.services.LoginAttemptEmailService;
import com.example.ecommercemarketplace.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class EcommerceMarketplaceApplication implements CommandLineRunner{

    private LoginAttemptEmailService loginAttemptEmailService;

    public static void main(String[] args) {
        SpringApplication.run(EcommerceMarketplaceApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        loginAttemptEmailService.registerFailureLogin("vitaliyverzyn25@gmail.com");
    }
}
