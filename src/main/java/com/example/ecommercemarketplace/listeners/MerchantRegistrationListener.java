package com.example.ecommercemarketplace.listeners;

import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.events.MerchantRegistrationEvent;
import com.example.ecommercemarketplace.services.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Async
@Component
@AllArgsConstructor
public class MerchantRegistrationListener implements ApplicationListener<MerchantRegistrationEvent> {

    private final EmailService emailService;

    @Override
    public void onApplicationEvent(MerchantRegistrationEvent event) {
        MerchantDto merchantDto = event.getMerchantDto();
        emailService.sendSuccessfulMerchantRegistrationMessage(merchantDto);
        log.info("New merchant successfully registered with publicId={}.", merchantDto.getPublicId());
    }
}
