package com.example.ecommercemarketplace.listeners;

import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.events.UserRegistrationEvent;
import com.example.ecommercemarketplace.services.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Async
@Slf4j
public class UserRegistrationListener implements ApplicationListener<UserRegistrationEvent> {

    private final EmailService emailService;

    @Override
    public void onApplicationEvent(UserRegistrationEvent event) {
        UserDto user = event.getUserDto();
        emailService.sendSuccessfulRegistrationMessage(user);
        log.info("New user successfully registered with publicId={}.", user.getPublicId());
    }
}
