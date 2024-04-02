package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.models.EmailConfirmationToken;
import com.example.ecommercemarketplace.security.CustomUserDetailsService;
import com.example.ecommercemarketplace.security.JwtService;
import com.example.ecommercemarketplace.services.AuthenticationService;
import com.example.ecommercemarketplace.services.EmailService;
import com.example.ecommercemarketplace.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private CustomUserDetailsService customUserDetailsService;
    private EmailService emailService;

    @Override
    public UserJwtTokenResponse login(UserLoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
        );

        authenticationManager.authenticate(token);

        if(token.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        UserDto userDto = userService.findByEmail(loginRequest.getEmail());
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);

        return new UserJwtTokenResponse(userDto.getPublicId(), jwtToken);

    }

    @Override
    public UserRegistrationResponse register(UserRegistrationRequest registrationRequest) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(registrationRequest,userDto);

        String tokenValue = UUID.randomUUID().toString();
        EmailConfirmationToken confirmationToken = EmailConfirmationToken.builder()
                .token(tokenValue)
                .build();

        userDto.setEmailConfirmationToken(confirmationToken);

        UserDto savedUser = userService.createUser(userDto);

        UserRegistrationResponse response = new UserRegistrationResponse();
        BeanUtils.copyProperties(savedUser,response);

        emailService.sendMessageWithVerificationCode(userDto.getEmail(), tokenValue);

        return response;
    }
}
