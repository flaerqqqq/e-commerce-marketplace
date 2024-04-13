package com.example.ecommercemarketplace.events;

import com.example.ecommercemarketplace.dto.MerchantDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MerchantRegistrationEvent extends ApplicationEvent {

    private MerchantDto merchantDto;

    public MerchantRegistrationEvent(Object source) {
        super(source);
    }

    public MerchantRegistrationEvent(Object source, MerchantDto merchantDto) {
        super(source);
        this.merchantDto = merchantDto;
    }
}
