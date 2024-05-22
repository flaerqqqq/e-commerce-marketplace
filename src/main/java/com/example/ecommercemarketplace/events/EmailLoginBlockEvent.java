package com.example.ecommercemarketplace.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmailLoginBlockEvent extends ApplicationEvent {

    private String email;

    public EmailLoginBlockEvent(Object source, String email) {
        super(source);
        this.email = email;
    }
}
