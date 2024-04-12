package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.MerchantDto;
import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.events.MerchantRegistrationEvent;
import com.example.ecommercemarketplace.events.UserRegistrationEvent;
import com.example.ecommercemarketplace.services.EmailConfirmationService;
import com.example.ecommercemarketplace.services.EmailConfirmationTokenService;
import com.example.ecommercemarketplace.services.MerchantService;
import com.example.ecommercemarketplace.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailConfirmationServiceImpl implements EmailConfirmationService {

    private final UserService userService;
    private final MerchantService merchantService;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void confirm(String token) {
        if (merchantService.existsByEmailConfirmationToken(token)){
            MerchantDto merchantDto = merchantService.findByEmailConfirmationToken(token);

            merchantDto.setEnabled(true);

            merchantService.updateMerchant(merchantDto);

            eventPublisher.publishEvent(new MerchantRegistrationEvent(this, merchantDto));
        } else if (userService.existsByEmailConfirmationToken(token)){
            UserDto userDto = userService.findByEmailConfirmationToken(token);

            userDto.setEnabled(true);

            userService.updateUser(userDto);

            eventPublisher.publishEvent(new UserRegistrationEvent(this, userDto));
        }
    }




}
