package com.example.ecommercemarketplace.listeners;


import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.events.UserLoginEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Async
@Component
public class UserLoginListener implements ApplicationListener<UserLoginEvent> {

    @Override
    public void onApplicationEvent(UserLoginEvent event) {
        UserDto userDto = event.getUserDto();
        log.info("User with publicId={} has logged in.", userDto.getPublicId());
    }
}
