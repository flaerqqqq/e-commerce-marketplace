package com.example.ecommercemarketplace.events;

import com.example.ecommercemarketplace.dto.UserDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegistrationEvent extends ApplicationEvent {

    private UserDto userDto;

    public UserRegistrationEvent(Object source) {
        super(source);
    }

    public UserRegistrationEvent(Object source, UserDto userDto) {
        super(source);
        this.userDto = userDto;
    }
}
