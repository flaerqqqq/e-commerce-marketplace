package com.example.ecommercemarketplace.tasks;

import com.example.ecommercemarketplace.models.LoginData;
import com.example.ecommercemarketplace.repositories.LoginDataRepository;
import com.example.ecommercemarketplace.services.LoginAttemptEmailService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@AllArgsConstructor
public class UnblockUserLoginTask {

    private final LoginAttemptEmailService loginAttemptEmailService;

    @Scheduled(fixedRate = 60000)
    public void unblockUserToLogin(){
        loginAttemptEmailService.unblockUsersLogin();
    }
}
