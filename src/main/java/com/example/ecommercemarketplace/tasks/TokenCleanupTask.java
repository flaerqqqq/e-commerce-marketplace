package com.example.ecommercemarketplace.tasks;

import com.example.ecommercemarketplace.services.PasswordResetService;
import com.example.ecommercemarketplace.services.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TokenCleanupTask {

    private PasswordResetService passwordResetService;
    private RefreshTokenService refreshTokenService;

    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredPasswordResetTokens() {
        passwordResetService.deleteExpiredTokens();
    }

    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredRefreshTokens() {
        refreshTokenService.deleteExpiredTokens();
    }


}
