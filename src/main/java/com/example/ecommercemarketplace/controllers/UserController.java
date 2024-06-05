package com.example.ecommercemarketplace.controllers;

import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public UserResponseDto findByPublicId(@PathVariable("id") String publicId) {
        UserDto user = userService.findUserByPublicId(publicId);
        return modelMapper.map(user, UserResponseDto.class);
    }

    @GetMapping
    public Page<UserResponseDto> findAll(Pageable pageable) {
        return userService.findAllUsers(pageable).map(userDto ->
                modelMapper.map(userDto, UserResponseDto.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public UserUpdateResponseDto updateUserFully(@RequestBody @Valid UserUpdateRequestDto updateRequest,
                                                 @PathVariable("id") String publicId) {
        UserDto userDto = modelMapper.map(updateRequest, UserDto.class);
        UserDto updatedUser = userService.updateUserFully(publicId, userDto);

        return modelMapper.map(updatedUser, UserUpdateResponseDto.class);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public UserUpdateResponseDto updateUserPatch(@RequestBody @Valid UserPatchUpdateRequestDto updateRequest,
                                                 @PathVariable("id") String publicId) {
        UserDto userDto = modelMapper.map(updateRequest, UserDto.class);
        UserDto updatedUser = userService.updateUserPatch(publicId, userDto);

        return modelMapper.map(updatedUser, UserUpdateResponseDto.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void deleteUser(@PathVariable("id") String publicId) {
        userService.removeUserByPublicId(publicId);
    }
}
