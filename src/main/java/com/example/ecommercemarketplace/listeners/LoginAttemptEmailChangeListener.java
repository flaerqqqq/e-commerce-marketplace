package com.example.ecommercemarketplace.listeners;

import com.example.ecommercemarketplace.events.EmailChangeEvent;
import com.example.ecommercemarketplace.services.LoginAttemptEmailService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class LoginAttemptEmailChangeListener implements ApplicationListener<EmailChangeEvent> {

    private final LoginAttemptEmailService loginAttemptEmailService;

    @Override
    public void onApplicationEvent(EmailChangeEvent event) {
        loginAttemptEmailService.unblockUserLogin(event.getUser().getEmail());
    }
}
