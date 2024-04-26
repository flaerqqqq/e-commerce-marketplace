package com.example.ecommercemarketplace.listeners;

import com.example.ecommercemarketplace.services.LoginAttemptEmailService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@AllArgsConstructor
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptEmailService loginAttemptEmailService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String email = event.getAuthentication().getName();
        loginAttemptEmailService.registerFailureLogin(email);
    }
}
