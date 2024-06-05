package com.example.ecommercemarketplace.events;

import com.example.ecommercemarketplace.dto.UserDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserLoginEvent extends ApplicationEvent {

    private final UserDto userDto;

    public UserLoginEvent(Object source, UserDto userDto) {
        super(source);
        this.userDto = userDto;
    }
}
