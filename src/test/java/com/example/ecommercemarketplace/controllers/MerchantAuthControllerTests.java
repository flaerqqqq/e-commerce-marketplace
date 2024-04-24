package com.example.ecommercemarketplace.controllers;

import com.example.ecommercemarketplace.dto.MerchantRegistrationRequestDto;
import com.example.ecommercemarketplace.dto.MerchantRegistrationResponseDto;
import com.example.ecommercemarketplace.models.enums.MerchantType;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class MerchantAuthControllerTests {

    @MockBean
    public AuthenticationService authenticationService;

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    private MerchantRegistrationRequestDto merchantRegistrationRequestDto;
    private MerchantRegistrationResponseDto merchantRegistrationResponseDto;

    @BeforeEach
    public void setUp() {
        merchantRegistrationRequestDto = MerchantRegistrationRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password1A!")
                .passwordConfirm("password1A!")
                .phoneNumber("+1234567890")
                .type(MerchantType.RETAILER)
                .websiteUrl("non")
                .build();
        merchantRegistrationResponseDto = MerchantRegistrationResponseDto.builder()
                .publicId("publicId")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .type(MerchantType.RETAILER)
                .websiteUrl("non")
                .registrationDate(LocalDateTime.now())
                .build();
    }

    @Test
    public void MerchantAuthController_RegisterMerchant_ShouldRegisterWith201Code_IfAllFieldsValid() throws Exception {
        when(authenticationService.registerMerchant(any(MerchantRegistrationRequestDto.class))).thenReturn(merchantRegistrationResponseDto);

        MvcResult result = mockMvc.perform(post("/api/auth/merchants/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(merchantRegistrationRequestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResultString = result.getResponse().getContentAsString();
        MerchantRegistrationResponseDto actualResponse = objectMapper.readValue(jsonResultString, MerchantRegistrationResponseDto.class);

        assertThat(actualResponse).isEqualTo(merchantRegistrationResponseDto);
    }

    @Test
    public void MerchantAuthController_RegisterMerchant_ShouldReturnBadRequest_IfFieldsNotValid() throws Exception {
        MerchantRegistrationRequestDto invalidRequest = MerchantRegistrationRequestDto.builder().build();

        mockMvc.perform(post("/api/auth/merchants/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
