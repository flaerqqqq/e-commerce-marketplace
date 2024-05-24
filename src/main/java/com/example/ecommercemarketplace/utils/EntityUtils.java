package com.example.ecommercemarketplace.utils;

import com.example.ecommercemarketplace.services.MerchantService;
import com.example.ecommercemarketplace.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EntityUtils {

    private UserService userService;
    private MerchantService merchantService;

    public String determineEntityName(String email) {
        if (merchantService.isMerchant(email)) {
            return "Merchant";
        } else if (userService.isUserEntity(email)) {
            return "User";
        } else {
            return "Unknown";
        }
    }
}