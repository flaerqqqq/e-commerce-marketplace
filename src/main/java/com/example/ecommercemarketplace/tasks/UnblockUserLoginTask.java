package com.example.ecommercemarketplace.tasks;

import com.example.ecommercemarketplace.services.LoginAttemptEmailService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UnblockUserLoginTask {

    private final LoginAttemptEmailService loginAttemptEmailService;

    @Scheduled(fixedRate = 60000)
    public void unblockUserToLogin() {
        loginAttemptEmailService.unblockUsersLogin();
    }
}
