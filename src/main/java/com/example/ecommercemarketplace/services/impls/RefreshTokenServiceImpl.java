package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.exceptions.RefreshTokenNotFoundException;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.RefreshToken;
import com.example.ecommercemarketplace.models.UserEntity;
import com.example.ecommercemarketplace.repositories.RefreshTokenRepository;
import com.example.ecommercemarketplace.security.JwtService;
import com.example.ecommercemarketplace.services.RefreshTokenService;
import com.example.ecommercemarketplace.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final Mapper<UserEntity, UserDto> userMapper;
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(String email) {
        UserEntity user = userMapper.mapFrom(userService.findByEmail(email));
        String jwtToken = jwtService.generateRefreshToken(email);
        RefreshToken refreshToken = RefreshToken.builder()
                .token(jwtToken)
                .user(user)
                .build();

        if (refreshTokenRepository.existsByUser(user)) {
            refreshToken.setId(refreshTokenRepository.findByUser(user).get().getId());
        }

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(() ->
                new RefreshTokenNotFoundException("Refresh token=%s is not found".formatted(token)));
    }

    @Override
    public RefreshToken validateToken(RefreshToken token) {
        jwtService.isValid(token.getToken());
        return token;
    }

    @Override
    public boolean existsByToken(String token) {
        return refreshTokenRepository.existsByToken(token);
    }

    @Override
    public void removeByToken(String token) {
        if (!refreshTokenRepository.existsByToken(token)) {
            throw new RefreshTokenNotFoundException("Refresh token=%s is not found".formatted(token));
        }
        refreshTokenRepository.removeByToken(token);
    }

    @Override
    public RefreshToken findByUser(UserDto userDto) {
        return refreshTokenRepository.findByUser(userMapper.mapFrom(userDto)).orElseThrow(() ->
                new RefreshTokenNotFoundException("Refresh for user with id=%s is not found".formatted(userDto.getPublicId())));
    }

    @Override
    public boolean existsByUser(UserDto userDto) {
        return refreshTokenRepository.existsByUser(userMapper.mapFrom(userDto));
    }

    @Override
    public void deleteExpiredTokens() {
        List<RefreshToken> tokens = refreshTokenRepository.findAll();

        for (var token : tokens) {
            try {
                jwtService.isValid(token.getToken());
            } catch (Exception e) {
                refreshTokenRepository.removeByToken(token.getToken());
            }
        }
    }

}
