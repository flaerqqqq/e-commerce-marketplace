package com.example.ecommercemarketplace.listeners;

import com.example.ecommercemarketplace.events.EmailLoginBlockEvent;
import com.example.ecommercemarketplace.utils.EntityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class EmailLoginBlockListener implements ApplicationListener<EmailLoginBlockEvent> {

    private final EntityUtils entityUtils;

    @Override
    public void onApplicationEvent(EmailLoginBlockEvent event) {
        String email = event.getEmail();
        String entityName = entityUtils.determineEntityName(email);
        log.info("User with email={} is suspended to login.", entityName);
    }
}
