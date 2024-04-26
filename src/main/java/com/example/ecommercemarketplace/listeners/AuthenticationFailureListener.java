package com.example.ecommercemarketplace.listeners;

import com.example.ecommercemarketplace.services.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@AllArgsConstructor
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String ipAddress = loginAttemptService.getClientIP();

        loginAttemptService.registerFailedLogin(ipAddress);
        log.info("User with IP_ADDRESS=%s failed to login".formatted(ipAddress));
    }
}
