package com.example.ecommercemarketplace.listeners;

import com.example.ecommercemarketplace.services.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String ipAddress = loginAttemptService.getClientIP();

        loginAttemptService.registerSuccessLogin(ipAddress);
        log.info("User with IP_ADDRESS=%s has logged in successfully".formatted(ipAddress));
    }
}
