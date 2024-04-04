package com.example.ecommercemarketplace.controllers;

import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/{id}")
    public UserResponseDto getUserByUserPublicId(@PathVariable("id") String id){
        UserDto user = userService.findUserByPublicId(id);
        UserResponseDto userResponseDto = new UserResponseDto();

        BeanUtils.copyProperties(user, userResponseDto);

        return userResponseDto;
    }

    @GetMapping
    public Page<UserResponseDto> getAllUsers(Pageable pageable){
        Page<UserResponseDto> page = userService.findAllUsers(pageable).map(userDto ->
        {
            UserResponseDto userResponseDto = new UserResponseDto();
            BeanUtils.copyProperties(userDto, userResponseDto);
            return userResponseDto;
        });
        return page;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserUpdateResponse updateUserFully(@RequestBody UserUpdateRequest userUpdateRequest,
                                              @PathVariable("id") String id){
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userUpdateRequest,userDto);

        UserDto updatedUser = userService.updateUserFully(id, userDto);

        UserUpdateResponse userUpdateResponse = new UserUpdateResponse();
        BeanUtils.copyProperties(updatedUser, userUpdateResponse);

        return userUpdateResponse;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserUpdateResponse updateUserPatch(@RequestBody UserUpdateRequest userUpdateRequest,
                                              @PathVariable("id") String id){
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userUpdateRequest,userDto);

        UserDto updatedUser = userService.updateUserPatch(id, userDto);

        UserUpdateResponse userUpdateResponse = new UserUpdateResponse();
        BeanUtils.copyProperties(updatedUser, userUpdateResponse);

        return userUpdateResponse;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") String id){
        userService.removeUserByPublicId(id);
    }
}
