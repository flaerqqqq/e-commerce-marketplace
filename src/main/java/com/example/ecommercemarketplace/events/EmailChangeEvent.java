package com.example.ecommercemarketplace.events;

import com.example.ecommercemarketplace.models.UserEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmailChangeEvent extends ApplicationEvent {

    public UserEntity user;

    public EmailChangeEvent(Object source) {
        super(source);
    }

    public EmailChangeEvent(Object source, UserEntity user){
        super(source);
        this.user = user;
    }
}
