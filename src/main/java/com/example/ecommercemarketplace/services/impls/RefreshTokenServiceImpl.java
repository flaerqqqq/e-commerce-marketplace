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

@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private Mapper<UserEntity, UserDto> userMapper;
    private UserService userService;
    private JwtService jwtService;
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(String email) {
        UserEntity user = userMapper.mapFrom(userService.findByEmail(email));
        String jwtToken = jwtService.generateRefreshToken(email);
        RefreshToken refreshToken = RefreshToken.builder()
                .token(jwtToken)
                .user(user)
                .build();

        if (refreshTokenRepository.existsByUser(user)){
            refreshToken.setId(refreshTokenRepository.findByUser(user).get().getId());
        }

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() ->
                new RefreshTokenNotFoundException("Refresh token=%s is not found".formatted(token)));

        return refreshToken;
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
        if (!refreshTokenRepository.existsByToken(token)){
            throw new RefreshTokenNotFoundException("Refresh token=%s is not found".formatted(token));
        }

        refreshTokenRepository.removeByToken(token);
    }

    @Override
    public RefreshToken findByUser(UserDto userDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByUser(userMapper.mapFrom(userDto)).orElseThrow(() ->
                new RefreshTokenNotFoundException("Refresh for user with id=%s is not found".formatted(userDto.getPublicId())));

        return refreshToken;
    }

    @Override
    public boolean existsByUser(UserDto userDto) {
        return refreshTokenRepository.existsByUser(userMapper.mapFrom(userDto));
    }

}
