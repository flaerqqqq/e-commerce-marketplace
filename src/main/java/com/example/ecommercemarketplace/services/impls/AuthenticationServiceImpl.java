package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.events.UserLoginEvent;
import com.example.ecommercemarketplace.events.UserRegistrationEvent;
import com.example.ecommercemarketplace.exceptions.RefreshTokenAlreadyExpired;
import com.example.ecommercemarketplace.exceptions.RefreshTokenNotFoundException;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.EmailConfirmationToken;
import com.example.ecommercemarketplace.models.RefreshToken;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.RefreshTokenRepository;
import com.example.ecommercemarketplace.security.CustomUserDetails;
import com.example.ecommercemarketplace.security.CustomUserDetailsService;
import com.example.ecommercemarketplace.security.JwtService;
import com.example.ecommercemarketplace.services.AuthenticationService;
import com.example.ecommercemarketplace.services.EmailService;
import com.example.ecommercemarketplace.services.RefreshTokenService;
import com.example.ecommercemarketplace.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;
    private final RefreshTokenService refreshTokenService;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final RefreshTokenRepository refreshTokenRepository;

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
        String refreshToken = refreshTokenService.createRefreshToken(userDto.getEmail()).getToken();

        eventPublisher.publishEvent(new UserLoginEvent(this, userDto));

        return UserJwtTokenResponse.builder()
                .publicId(userDto.getPublicId())
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public UserRegistrationResponse register(UserRegistrationRequest registrationRequest){
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

    @Override
    public UserJwtTokenResponse refresh(RefreshTokenRequestDto refreshTokenRequestDto) {
        String requestRefreshToken = refreshTokenRequestDto.getToken();

        if(!refreshTokenService.existsByToken(requestRefreshToken)){
            throw new RefreshTokenNotFoundException("Refresh token=%s is not found".formatted(requestRefreshToken));
        }

        refreshTokenService.removeByToken(requestRefreshToken);

        try {
            jwtService.isValid(requestRefreshToken);
        } catch (Exception e){
            throw new RefreshTokenAlreadyExpired("Refresh token=%s is expired".formatted(requestRefreshToken));
        }

        String userEmail = jwtService.extractEmail(requestRefreshToken);
        UserEntity user = userMapper.mapFrom(userService.findByEmail(userEmail));

        UserDetails userDetails = new CustomUserDetails(user);
        String jwtToken = jwtService.generateToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userEmail);

        return UserJwtTokenResponse.builder()
                .publicId(user.getPublicId())
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }
}
