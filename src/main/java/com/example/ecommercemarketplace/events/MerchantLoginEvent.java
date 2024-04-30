package com.example.ecommercemarketplace.events;

import com.example.ecommercemarketplace.dto.MerchantDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MerchantLoginEvent extends ApplicationEvent {

    private final MerchantDto merchantDto;

    public MerchantLoginEvent(Object source, MerchantDto merchantDto) {
        super(source);
        this.merchantDto = merchantDto;
    }
}