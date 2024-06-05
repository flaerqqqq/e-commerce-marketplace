package com.example.ecommercemarketplace.listeners;

import com.example.ecommercemarketplace.events.EmailChangeEvent;
import com.example.ecommercemarketplace.services.LoginAttemptEmailService;
import com.example.ecommercemarketplace.utils.EntityUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class LoginAttemptEmailChangeListener implements ApplicationListener<EmailChangeEvent> {

    private final LoginAttemptEmailService loginAttemptEmailService;
    private final EntityUtils entityUtils;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void onApplicationEvent(EmailChangeEvent event) {
        String email = event.getUser().getEmail();
        loginAttemptEmailService.unblockUserLogin(email);

        String entityName = entityUtils.determineEntityName(email);
        log.info("{} with email={} has been unblocked to login, because of password change", entityName, email);
    }
}
