package com.example.ecommercemarketplace.tasks;

import com.example.ecommercemarketplace.repositories.PasswordResetTokenRepository;
import com.example.ecommercemarketplace.services.PasswordResetService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TokenCleanupTask {

    private PasswordResetService passwordResetService;

    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredTokens(){
        passwordResetService.deleteExpiredTokens();
    }

}
