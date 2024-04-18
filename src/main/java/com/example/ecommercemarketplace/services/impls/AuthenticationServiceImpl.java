package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.events.MerchantLoginEvent;
import com.example.ecommercemarketplace.events.UserLoginEvent;
import com.example.ecommercemarketplace.exceptions.MissingAuthorizationHeaderException;
import com.example.ecommercemarketplace.exceptions.RefreshTokenAlreadyExpired;
import com.example.ecommercemarketplace.exceptions.RefreshTokenNotFoundException;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.EmailConfirmationToken;
import com.example.ecommercemarketplace.models.RefreshToken;
import com.example.ecommercemarketplace.models.Role;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.RoleRepository;
import com.example.ecommercemarketplace.security.CustomUserDetails;
import com.example.ecommercemarketplace.security.CustomUserDetailsService;
import com.example.ecommercemarketplace.security.JwtService;
import com.example.ecommercemarketplace.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String BEARER_PREFIX = "Bearer ";
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final EmailService emailService;
    private final MerchantService merchantService;
    private final ApplicationEventPublisher eventPublisher;
    private final RefreshTokenService refreshTokenService;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final Mapper<UserDto, MerchantDto> userMerchantMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserJwtTokenResponseDto login(UserLoginRequestDto loginRequest) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
        );

        authenticationManager.authenticate(token);

        if (token.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        UserDto userDto = userService.findByEmail(loginRequest.getEmail());

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(userDto.getEmail()).getToken();

        if (merchantService.isMerchant(loginRequest.getEmail())) {
            eventPublisher.publishEvent(new MerchantLoginEvent(this, userMerchantMapper.mapTo(userDto)));
        } else if (userService.isUserEntity(loginRequest.getEmail())) {
            eventPublisher.publishEvent(new UserLoginEvent(this, userDto));
        }

        return UserJwtTokenResponseDto.builder()
                .publicId(userDto.getPublicId())
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public UserRegistrationResponseDto register(UserRegistrationRequestDto registrationRequest) {
        UserDto userDto = new UserDto();
        userDto.setRoles(List.of(roleRepository.findByName(Role.RoleName.ROLE_USER).get()));
        BeanUtils.copyProperties(registrationRequest, userDto);

        EmailConfirmationToken confirmationToken = emailConfirmationTokenService.buildEmailConfirmationToken();
        userDto.setEmailConfirmationToken(confirmationToken);

        UserDto savedUser = userService.createUser(userDto);

        UserRegistrationResponseDto response = new UserRegistrationResponseDto();
        BeanUtils.copyProperties(savedUser, response);

        emailService.sendMessageWithVerificationCode(userDto.getEmail(), confirmationToken.getToken());

        return response;
    }

    @Override
    public UserJwtTokenResponseDto refresh(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            throw new MissingAuthorizationHeaderException("Missing authorization header or 'bearer' prefix");
        }

        String requestRefreshToken = header.substring(7);

        if (!refreshTokenService.existsByToken(requestRefreshToken)) {
            throw new RefreshTokenNotFoundException("Refresh token=%s is not found".formatted(requestRefreshToken));
        }

        refreshTokenService.removeByToken(requestRefreshToken);

        try {
            jwtService.isValid(requestRefreshToken);
        } catch (Exception e) {
            throw new RefreshTokenAlreadyExpired("Refresh token=%s is expired".formatted(requestRefreshToken));
        }

        String userEmail = jwtService.extractEmail(requestRefreshToken);
        UserEntity user = userMapper.mapFrom(userService.findByEmail(userEmail));

        UserDetails userDetails = new CustomUserDetails(user);
        String jwtToken = jwtService.generateToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userEmail);

        return UserJwtTokenResponseDto.builder()
                .publicId(user.getPublicId())
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public MerchantRegistrationResponseDto registerMerchant(MerchantRegistrationRequestDto registrationRequestDto) {
        MerchantDto merchantDto = new MerchantDto();
        merchantDto.setRoles(List.of(roleRepository.findByName(Role.RoleName.ROLE_USER).get(),
                roleRepository.findByName(Role.RoleName.ROLE_MERCHANT).get()));
        merchantDto.setRegistrationDate(LocalDateTime.now());
        BeanUtils.copyProperties(registrationRequestDto, merchantDto);

        EmailConfirmationToken token = emailConfirmationTokenService.buildEmailConfirmationToken();
        merchantDto.setEmailConfirmationToken(token);

        MerchantDto savedMerchant = merchantService.createMerchant(merchantDto);

        MerchantRegistrationResponseDto response = new MerchantRegistrationResponseDto();
        BeanUtils.copyProperties(savedMerchant, response);

        emailService.sendMessageWithVerificationCode(merchantDto.getEmail(), token.getToken());

        return response;
    }


}
