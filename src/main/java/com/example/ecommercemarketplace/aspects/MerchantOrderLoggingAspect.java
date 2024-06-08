package com.example.ecommercemarketplace.aspects;

import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.dto.StatusChangeRequestDto;
import com.example.ecommercemarketplace.models.Merchant;
import com.example.ecommercemarketplace.services.MerchantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class MerchantOrderLoggingAspect {

    private final MerchantService merchantService;

    @After("execution(* com.example.ecommercemarketplace.controllers.MerchantOrderController.changeMerchantOrderStatus(..))" +
            "&& args(requestDto, id, authentication)")
    public void afterChangeMerchantOrderStatus(StatusChangeRequestDto requestDto, Long id, Authentication authentication) {
        MerchantDto merchant = merchantService.findByEmail(authentication.getName());
        log.info("Merchant with publicId={} changed the status of merchant order with id={} to {}.", merchant.getPublicId(), id, requestDto.getStatus());
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.MerchantOrderController.getMerchantOrdersByMerchant(..))" +
            "&& args(authentication)")
    public void afterGetMerchantOrdersByMerchant(Authentication authentication) {
        MerchantDto merchant = merchantService.findByEmail(authentication.getName());
        log.info("Merchant with publicId={} retrieved all his merchant orders.", merchant.getPublicId());
    }
}
