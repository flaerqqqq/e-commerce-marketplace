package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.UserDto;
import com.example.ecommercemarketplace.dto.UserRequestDto;
import com.example.ecommercemarketplace.dto.UserResponseDto;
import com.example.ecommercemarketplace.exceptions.UserNotFoundException;
import com.example.ecommercemarketplace.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponseDto userResponseDto;
    private UserDto userDto;
    private UserRequestDto userRequestDto;

    @BeforeEach
    public void init(){
        userResponseDto = UserResponseDto.builder()
                .publicId("publicId")
                .firstName("Nikolay")
                .lastName("Twink")
                .email("test@gmail.com")
                .phoneNumber("+123456789")
                .build();

        userDto = UserDto.builder()
                .publicId("publicId")
                .firstName("Nikolay")
                .lastName("Twink")
                .email("test@gmail.com")
                .phoneNumber("+123456789")
                .password("1234")
                .build();
        userRequestDto = UserRequestDto.builder()
                .publicId("publicId")
                .firstName("Nikolay")
                .lastName("Twink")
                .email("test@gmail.com")
                .phoneNumber("+123456789")
                .build();
    }

    @Test
    public void UserController_GetUserByUserPublicId_ShouldReturnCorrectUser() throws Exception{
        String publicId = userResponseDto.getPublicId();

        when(userService.findUserByPublicId(anyString())).thenReturn(userDto);

        MvcResult mvcResult = mockMvc.perform(get("/api/users/{id}", userDto.getPublicId()))
                .andExpect(status().isOk())
                .andReturn();

        verify(userService).findUserByPublicId(publicId);

        String jsonRequest = mvcResult.getResponse().getContentAsString();
        UserResponseDto actualResponseDto = objectMapper.readValue(jsonRequest, UserResponseDto.class);

        assertThat(userResponseDto).isEqualTo(actualResponseDto);
    }

    @Test
    public void UserController_GetUserByUserPublicId_ShouldReturnNotFoundStatus() throws Exception {
        String publicId = userResponseDto.getPublicId();

        when(userService.findUserByPublicId(anyString())).thenThrow(new UserNotFoundException(publicId));

        mockMvc.perform(get("/api/users/{id}", publicId))
                .andExpect(status().isNotFound());

        verify(userService).findUserByPublicId(publicId);

    }

    @Test
    public void UserController_GetAllUsers_ShouldReturnPageOfUsers(){

    }

    @Test
    public void UserController_UpdateUserFully_ShouldUpdate( ) {

    }
}
