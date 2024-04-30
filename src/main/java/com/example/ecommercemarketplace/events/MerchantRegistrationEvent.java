package com.example.ecommercemarketplace.events;

import com.example.ecommercemarketplace.dto.MerchantDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MerchantRegistrationEvent extends ApplicationEvent {

    private MerchantDto merchant;


    public MerchantRegistrationEvent(Object source, MerchantDto merchant) {
        super(source);
        this.merchant = merchant;
    }
}
