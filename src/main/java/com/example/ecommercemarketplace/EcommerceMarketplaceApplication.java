package com.example.ecommercemarketplace;

import com.example.ecommercemarketplace.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class EcommerceMarketplaceApplication{

    private final UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(EcommerceMarketplaceApplication.class, args);
    }


}
