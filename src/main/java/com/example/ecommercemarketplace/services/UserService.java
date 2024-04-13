package com.example.ecommercemarketplace.services;

import com.example.ecommercemarketplace.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserDto findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailConfirmationToken(String token);

    boolean isUserEntity(String email);

    UserDto createUser(UserDto userDto);

    UserDto findByEmailConfirmationToken(String token);

    UserDto updateUser(UserDto userDto);

    UserDto findUserByPublicId(String publicId);

    Page<UserDto> findAllUsers(Pageable pageable);

    UserDto updateUserFully(String publicId, UserDto userDto);

    UserDto updateUserPatch(String publicId, UserDto userDto);

    void removeUserByPublicId(String publicId);
}
