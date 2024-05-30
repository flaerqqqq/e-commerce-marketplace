package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.events.MerchantLoginEvent;
import com.example.ecommercemarketplace.events.UserLoginEvent;
import com.example.ecommercemarketplace.exceptions.MissingAuthorizationHeaderException;
import com.example.ecommercemarketplace.exceptions.RefreshTokenAlreadyExpired;
import com.example.ecommercemarketplace.exceptions.RefreshTokenNotFoundException;
import com.example.ecommercemarketplace.mappers.UserMapper;
import com.example.ecommercemarketplace.mappers.UserMerchantMapper;
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
import org.modelmapper.ModelMapper;
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
    private final UserMapper userMapper;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final UserMerchantMapper userMerchantMapper;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserJwtTokenResponseDto login(UserLoginRequestDto loginRequest) {
        authenticateUser(loginRequest);

        UserDto userDto = userService.findByEmail(loginRequest.getEmail());

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(userDto.getEmail()).getToken();

        publishLoginEvent(loginRequest, userDto);

        return UserJwtTokenResponseDto.builder()
                .publicId(userDto.getPublicId())
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public UserJwtTokenResponseDto refresh(HttpServletRequest request) {
        String requestRefreshToken = validateHttpHeader(request);

        refreshTokenService.removeByToken(requestRefreshToken);

        validateRefreshTokenToken(requestRefreshToken);

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
    public UserRegistrationResponseDto register(UserRegistrationRequestDto registrationRequest) {
        UserDto userDto = modelMapper.map(registrationRequest, UserDto.class);
        userDto.setRoles(List.of(roleRepository.findByName(Role.RoleName.ROLE_USER).get()));

        EmailConfirmationToken confirmationToken = emailConfirmationTokenService.buildEmailConfirmationToken();
        userDto.setEmailConfirmationToken(confirmationToken);

        UserDto savedUser = userService.createUser(userDto);
        UserRegistrationResponseDto response = modelMapper.map(savedUser, UserRegistrationResponseDto.class);

        emailService.sendMessageWithVerificationCode(userDto.getEmail(), confirmationToken.getToken());

        return response;
    }

    @Override
    public MerchantRegistrationResponseDto registerMerchant(MerchantRegistrationRequestDto registrationRequestDto) {
        MerchantDto merchantDto = modelMapper.map(registrationRequestDto, MerchantDto.class);

        merchantDto.setRoles(List.of(roleRepository.findByName(Role.RoleName.ROLE_USER).get(),
                roleRepository.findByName(Role.RoleName.ROLE_MERCHANT).get()));
        merchantDto.setRegistrationDate(LocalDateTime.now());

        EmailConfirmationToken token = emailConfirmationTokenService.buildEmailConfirmationToken();
        merchantDto.setEmailConfirmationToken(token);

        MerchantDto savedMerchant = merchantService.createMerchant(merchantDto);
        MerchantRegistrationResponseDto response = modelMapper.map(savedMerchant, MerchantRegistrationResponseDto.class);

        emailService.sendMessageWithVerificationCode(merchantDto.getEmail(), token.getToken());

        return response;
    }

    private void publishLoginEvent(UserLoginRequestDto loginRequestDto, UserDto userDto) {
        if (merchantService.isMerchant(loginRequestDto.getEmail())) {
            eventPublisher.publishEvent(new MerchantLoginEvent(this, userMerchantMapper.mapTo(userDto)));
        } else if (userService.isUserEntity(loginRequestDto.getEmail())) {
            eventPublisher.publishEvent(new UserLoginEvent(this, userDto));
        }
    }

    private void authenticateUser(UserLoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(), loginRequestDto.getPassword()
        );

        authenticationManager.authenticate(token);

        if (token.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }

    private String validateHttpHeader(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            throw new MissingAuthorizationHeaderException("Missing authorization header or 'bearer' prefix");
        }

        String requestRefreshToken = header.substring(7);

        if (!refreshTokenService.existsByToken(requestRefreshToken)) {
            throw new RefreshTokenNotFoundException("Refresh token=%s is not found".formatted(requestRefreshToken));
        }

        return requestRefreshToken;
    }

    private void validateRefreshTokenToken(String requestRefreshToken) {
        try {
            jwtService.isValid(requestRefreshToken);
        } catch (Exception e) {
            throw new RefreshTokenAlreadyExpired("Refresh token=%s is expired".formatted(requestRefreshToken));
        }
    }


}
