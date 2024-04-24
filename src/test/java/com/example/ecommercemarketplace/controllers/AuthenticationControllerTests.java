package com.example.ecommercemarketplace.controllers;

import com.example.ecommercemarketplace.dto.UserJwtTokenResponseDto;
import com.example.ecommercemarketplace.dto.UserLoginRequestDto;
import com.example.ecommercemarketplace.dto.UserRegistrationRequestDto;
import com.example.ecommercemarketplace.dto.UserRegistrationResponseDto;
import com.example.ecommercemarketplace.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class AuthenticationControllerTests {

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRegistrationRequestDto userRegistrationRequestDto;
    private UserRegistrationResponseDto userRegistrationResponseDto;
    private UserLoginRequestDto userLoginRequestDto;
    private UserJwtTokenResponseDto jwtTokenResponseDto;

    @BeforeEach
    public void setUp() {
        userRegistrationRequestDto = UserRegistrationRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password1A!")
                .passwordConfirm("password1A!")
                .phoneNumber("+1234567890")
                .build();
        userRegistrationResponseDto = UserRegistrationResponseDto.builder()
                .publicId("publicId")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .build();
        userLoginRequestDto = UserLoginRequestDto.builder()
                .email("test")
                .password("test")
                .build();
        jwtTokenResponseDto = UserJwtTokenResponseDto.builder()
                .publicId("publicId")
                .token("token")
                .refreshToken("refreshToken")
                .build();
    }

    @Test
    public void AuthenticationController_Register_ShouldRegisterWith201Code_IfAllFieldsValid() throws Exception {
        when(authenticationService.register(any(UserRegistrationRequestDto.class))).thenReturn(userRegistrationResponseDto);

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRegistrationRequestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResultString = result.getResponse().getContentAsString();
        UserRegistrationResponseDto actualResponse = objectMapper.readValue(jsonResultString, UserRegistrationResponseDto.class);

        assertThat(actualResponse).isEqualTo(userRegistrationResponseDto);
    }

    @Test
    public void AuthenticationController_Register_ShouldReturnBadRequest_IfFieldsNotValid() throws Exception {
        UserRegistrationRequestDto invalidRequest = UserRegistrationRequestDto.builder().build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void AuthenticationController_Login_ShouldLoginWith200_IfUserProvideCorrectCredentials() throws Exception {
        when(authenticationService.login(any(UserLoginRequestDto.class))).thenReturn(jwtTokenResponseDto);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userLoginRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        UserJwtTokenResponseDto actualResponse = objectMapper.readValue(jsonResult, UserJwtTokenResponseDto.class);

        assertThat(actualResponse).isEqualTo(jwtTokenResponseDto);
    }

    @Test
    public void AuthenticationController_Login_ShouldFail_IfUserProvideInvalidCredentials() throws Exception {
        when(authenticationService.login(any(UserLoginRequestDto.class))).thenThrow(UsernameNotFoundException.class);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userLoginRequestDto)))
                .andExpect(status().isNotFound());
    }
}
