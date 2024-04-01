package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.UserDto;

public interface UserService {

    UserDto findByEmail(String email);

    boolean existsByEmail(String email);

    UserDto createUser(UserDto userDto);

}
