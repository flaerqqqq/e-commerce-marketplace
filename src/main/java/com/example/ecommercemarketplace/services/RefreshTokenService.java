package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.models.RefreshToken;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(String email);

    RefreshToken findByToken(String token);

    RefreshToken validateToken(RefreshToken token);

    boolean existsByToken(String token);

    void removeByToken(String token);

    RefreshToken findByUser(UserDto userDto);

    boolean existsByUser(UserDto userDto);



}
