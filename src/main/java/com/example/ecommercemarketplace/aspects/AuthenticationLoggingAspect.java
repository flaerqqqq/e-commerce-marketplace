package com.example.ecommercemarketplace.aspects;

import com.example.ecommercemarketplace.dto.PasswordResetConfirmationRequestDto;
import com.example.ecommercemarketplace.dto.PasswordResetRequestDto;
import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.security.JwtService;
import com.example.ecommercemarketplace.services.UserService;
import com.example.ecommercemarketplace.utils.EntityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class AuthenticationLoggingAspect {

    private final JwtService jwtService;
    private final UserService userService;
    private final EntityUtils entityUtils;


    @After("execution(* com.example.ecommercemarketplace.controllers.PasswordResetController.requestPasswordReset(..))")
    public void afterPasswordResetRequest(final JoinPoint joinPoint) {
        PasswordResetRequestDto requestDto = (PasswordResetRequestDto) joinPoint.getArgs()[0];
        String entityName = entityUtils.determineEntityName(requestDto.getEmail());
        log.info("{} with email={} requested to reset password.", entityName, requestDto.getEmail());
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.PasswordResetController.confirmPasswordReset(..))")
    public void afterPasswordResetConfirm(final JoinPoint joinPoint) {
        PasswordResetConfirmationRequestDto requestDto = (PasswordResetConfirmationRequestDto) joinPoint.getArgs()[0];
        String email = jwtService.extractEmail(requestDto.getToken());
        String entityName = entityUtils.determineEntityName(email);
        log.info("{} with email={} confirmed password reset.", entityName, email);
    }

    @After("execution(* com.example.ecommercemarketplace.controllers.EmailConfirmationController.confirm(..))")
    public void afterConfirmEmail(final JoinPoint joinPoint) {
        String token = (String) joinPoint.getArgs()[0];
        UserDto userDto = userService.findByEmailConfirmationToken(token);
        String entityName = entityUtils.determineEntityName(userDto.getEmail());
        log.info("{} with email={} has verified its email.", entityName,  userDto.getEmail());
    }
}
