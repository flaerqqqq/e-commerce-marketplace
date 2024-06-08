package com.example.ecommercemarketplace.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class IpLoginBlockEvent extends ApplicationEvent {

    private final String ipAddress;

    public IpLoginBlockEvent(Object source, String ipAddress) {
        super(source);
        this.ipAddress = ipAddress;
    }
}
