package com.example.ecommercemarketplace.listeners;

import com.example.ecommercemarketplace.events.EmailLoginBlockEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailLoginBlockListener implements ApplicationListener<EmailLoginBlockEvent> {
    @Override
    public void onApplicationEvent(EmailLoginBlockEvent event) {
        log.info("User with email={} is suspended to login.", event.getEmail());
    }
}
