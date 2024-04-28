package com.example.ecommercemarketplace.controllers;


import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.exceptions.UserNotFoundException;
import com.example.ecommercemarketplace.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String publicId = "publicId";
    private UserResponseDto userResponseDto;
    private UserDto userDto;
    private UserUpdateRequestDto userUpdateRequestDto;
    private UserUpdateResponseDto userUpdateResponseDto;

    @BeforeEach
    public void init() {
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
        userUpdateRequestDto = UserUpdateRequestDto.builder()
                .firstName("Nikolay")
                .lastName("Twink")
                .phoneNumber("+123456789")
                .build();
        userUpdateResponseDto = UserUpdateResponseDto.builder()
                .publicId("publicId")
                .firstName("Nikolay")
                .lastName("Twink")
                .phoneNumber("+123456789")
                .build();
    }

    @Test
    public void UserController_GetUserByUserPublicId_ShouldReturnCorrectUser() throws Exception {
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
    public void UserController_GetUserByUserPublicId_ShouldReturnNotFoundStatus_IfUserWithIdNotExist() throws Exception {
        when(userService.findUserByPublicId(anyString())).thenThrow(new UserNotFoundException(publicId));

        mockMvc.perform(get("/api/users/{id}", publicId))
                .andExpect(status().isNotFound());

        verify(userService).findUserByPublicId(publicId);
    }

    @Test
    public void UserController_GetAllUsers_ShouldReturnPageOfUsers() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;
        List<UserDto> users = generateUsers(pageSize);
        Page<UserDto> pageOfUsers = new PageImpl<>(users, PageRequest.of(pageNumber, pageSize), users.size());

        when(userService.findAllUsers(any(Pageable.class))).thenReturn(pageOfUsers);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(pageSize)));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"USER", "ADMIN"})
    public void UserController_UpdateUserFully_ShouldUpdate() throws Exception {
        when(userService.updateUserFully(anyString(), any(UserDto.class))).thenReturn(userDto);

        MvcResult mvcResult = mockMvc.perform(put("/api/users/{id}", publicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequestDto))
                ).andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        UserUpdateResponseDto userUpdateResponseDto1 = objectMapper.readValue(jsonResponse, UserUpdateResponseDto.class);

        assertThat(userUpdateResponseDto1).isEqualTo(userUpdateResponseDto);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"USER", "ADMIN"})
    public void UserController_UpdateUserFully_ShouldReturnNotFoundStatus_IfUserWithIdNotExist() throws Exception {
        when(userService.updateUserFully(anyString(), any(UserDto.class))).thenThrow(new UserNotFoundException(publicId));

        mockMvc.perform(put("/api/users/{id}", publicId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateRequestDto))
        ).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"USER", "ADMIN"})
    public void UserController_UpdateUserFully_ShouldReturnBadRequestStatus_IfUserEnterInvalidData() throws Exception {
        UserUpdateRequestDto invalidUserRequest = new UserUpdateRequestDto();

        mockMvc.perform(put("/api/users/{id}", publicId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUserRequest))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"USER", "ADMIN"})
    public void UserController_UpdateUserPatch_ShouldUpdate() throws Exception {
        when(userService.updateUserPatch(anyString(), any(UserDto.class))).thenReturn(userDto);

        MvcResult mvcResult = mockMvc.perform(patch("/api/users/{id}", publicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        UserUpdateResponseDto userUpdateResponseDto1 = objectMapper.readValue(jsonResponse, UserUpdateResponseDto.class);

        assertThat(userUpdateResponseDto1).isEqualTo(userUpdateResponseDto);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"USER", "ADMIN"})
    public void UserController_UpdateUserPatch_ShouldReturnNotFoundStatus_IfUserWithIdNotExist() throws Exception {
        when(userService.updateUserPatch(anyString(), any(UserDto.class))).thenThrow(new UserNotFoundException(publicId));

        mockMvc.perform(patch("/api/users/{id}", publicId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateRequestDto))
        ).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"USER", "ADMIN"})
    public void UserController_UpdateUserPatch_ShouldReturnBadRequestStatus_IfUserEnterInvalidData() throws Exception {
        UserPatchUpdateRequestDto invalidUserRequest = UserPatchUpdateRequestDto.builder()
                .phoneNumber("+")
                .build();

        mockMvc.perform(patch("/api/users/{id}", publicId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUserRequest))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"USER", "ADMIN"})
    public void UserController_DeleteUser_ShouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", publicId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"USER", "ADMIN"})
    public void UserController_DeleteUser_ShouldReturnNotFoundStatus_IfUserWithIdNotExist() throws Exception {
        doThrow(new UserNotFoundException(publicId)).when(userService).removeUserByPublicId(anyString());

        mockMvc.perform(delete("/api/users/{id}", publicId))
                .andExpect(status().isNotFound());
    }

    private List<UserDto> generateUsers(int count) {
        List<UserDto> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            UserDto user = new UserDto();
            // Set user properties
            users.add(user);
        }
        return users;
    }

}
