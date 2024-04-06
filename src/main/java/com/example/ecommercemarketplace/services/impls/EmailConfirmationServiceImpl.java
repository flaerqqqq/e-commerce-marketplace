package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.events.UserRegistrationEvent;
import com.example.ecommercemarketplace.services.EmailConfirmationService;
import com.example.ecommercemarketplace.services.EmailConfirmationTokenService;
import com.example.ecommercemarketplace.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailConfirmationServiceImpl implements EmailConfirmationService {

    private final UserService userService;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void confirm(String token) {
        if (emailConfirmationTokenService.existsByToken(token)){
            UserDto user = userService.findByEmailConfirmationToken(token);

            user.setEnabled(true);
            userService.updateUser(user);

            eventPublisher.publishEvent(new UserRegistrationEvent(this, user));
        }
    }
}
