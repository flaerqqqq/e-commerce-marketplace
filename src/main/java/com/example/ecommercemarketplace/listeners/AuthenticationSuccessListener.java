package com.example.ecommercemarketplace.listeners;

import com.example.ecommercemarketplace.services.LoginAttemptEmailService;
import com.example.ecommercemarketplace.services.LoginAttemptIPService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final LoginAttemptIPService loginAttemptIPService;
    private final LoginAttemptEmailService loginAttemptEmailService;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String ipAddress = loginAttemptIPService.getClientIP();
        String email = event.getAuthentication().getName();

        loginAttemptIPService.registerSuccessLogin(ipAddress);
        loginAttemptEmailService.registerSuccessfulLogin(email);
    }
}
