package com.example.ecommercemarketplace.controllers;

import com.example.ecommercemarketplace.dto.*;
import com.example.ecommercemarketplace.exceptions.MerchantNotFoundException;
import com.example.ecommercemarketplace.models.enums.MerchantStatus;
import com.example.ecommercemarketplace.models.enums.MerchantType;
import com.example.ecommercemarketplace.services.MerchantService;
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
public class MerchantControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MerchantService merchantService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String publicId = "publicId";
    private MerchantDto merchantDto;
    private MerchantResponseDto merchantResponseDto;
    private MerchantUpdateRequestDto merchantUpdateRequestDto;
    private MerchantUpdateResponseDto merchantUpdateResponseDto;

    @BeforeEach
    public void init() {
        merchantResponseDto = MerchantResponseDto.builder()
                .publicId("publicId")
                .firstName("Nikolay")
                .lastName("Twink")
                .email("test@gmail.com")
                .phoneNumber("+123456789")
                .type(MerchantType.RETAILER)
                .status(MerchantStatus.ACTIVE)
                .websiteUrl("url")
                .build();
        merchantDto = MerchantDto.builder()
                .publicId("publicId")
                .firstName("Nikolay")
                .lastName("Twink")
                .email("test@gmail.com")
                .phoneNumber("+123456789")
                .password("1234")
                .type(MerchantType.RETAILER)
                .status(MerchantStatus.ACTIVE)
                .websiteUrl("url")
                .build();
        merchantUpdateRequestDto = MerchantUpdateRequestDto.builder()
                .publicId("publicId")
                .firstName("Nikolay")
                .lastName("Twink")
                .phoneNumber("+123456789")
                .type(MerchantType.RETAILER)
                .websiteUrl("url")
                .build();
        merchantUpdateResponseDto = MerchantUpdateResponseDto.builder()
                .publicId("publicId")
                .firstName("Nikolay")
                .lastName("Twink")
                .phoneNumber("+123456789")
                .type(MerchantType.RETAILER)
                .websiteUrl("url")
                .build();
    }

    @Test
    public void MerchantController_GetMerchantByMerchantPublicId_ShouldReturnCorrectMerchant() throws Exception {
        when(merchantService.findMerchantByPublicId(anyString())).thenReturn(merchantDto);

        MvcResult mvcResult = mockMvc.perform(get("/api/merchants/{id}", merchantDto.getPublicId()))
                .andExpect(status().isOk())
                .andReturn();

        verify(merchantService).findMerchantByPublicId(publicId);

        String jsonRequest = mvcResult.getResponse().getContentAsString();
        MerchantResponseDto actualResponseDto = objectMapper.readValue(jsonRequest, MerchantResponseDto.class);

        assertThat(merchantResponseDto).isEqualTo(actualResponseDto);
    }

    @Test
    public void MerchantController_GetMerchantByMerchantPublicId_ShouldReturnNotFoundStatus_IfMerchantWithIdNotExist() throws Exception {
        when(merchantService.findMerchantByPublicId(anyString())).thenThrow(new MerchantNotFoundException(publicId));

        mockMvc.perform(get("/api/merchants/{id}", publicId))
                .andExpect(status().isNotFound());

        verify(merchantService).findMerchantByPublicId(publicId);
    }

    @Test
    public void MerchantController_GetAllMerchants_ShouldReturnPageOfMerchants() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;
        List<MerchantDto> merchants = generateMerchants(pageSize);
        Page<MerchantDto> pageOfMerchants = new PageImpl<>(merchants, PageRequest.of(pageNumber, pageSize), merchants.size());

        when(merchantService.findAllMerchants(any(Pageable.class))).thenReturn(pageOfMerchants);

        mockMvc.perform(get("/api/merchants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(pageSize)));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"MERCHANT", "ADMIN"})
    public void MerchantController_UpdateMerchantFully_ShouldUpdate() throws Exception {
        when(merchantService.updateMerchantFully(anyString(), any(MerchantDto.class))).thenReturn(merchantDto);

        MvcResult mvcResult = mockMvc.perform(put("/api/merchants/{id}", publicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(merchantUpdateRequestDto))
                ).andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        MerchantUpdateResponseDto merchantUpdateResponseDto1 = objectMapper.readValue(jsonResponse, MerchantUpdateResponseDto.class);

        assertThat(merchantUpdateResponseDto1).isEqualTo(merchantUpdateResponseDto);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"MERCHANT", "ADMIN"})
    public void MerchantController_UpdateMerchantFully_ShouldReturnNotFoundStatus_IfMerchantWithIdNotExist() throws Exception {
        when(merchantService.updateMerchantFully(anyString(), any(MerchantDto.class))).thenThrow(new MerchantNotFoundException(publicId));

        mockMvc.perform(put("/api/merchants/{id}", publicId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(merchantUpdateRequestDto))
        ).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"MERCHANT", "ADMIN"})
    public void MerchantController_UpdateMerchantFully_ShouldReturnBadRequestStatus_IfMerchantEnterInvalidData() throws Exception {
        MerchantUpdateRequestDto invalidMerchantRequest = new MerchantUpdateRequestDto();

        mockMvc.perform(put("/api/merchants/{id}", publicId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidMerchantRequest))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"MERCHANT", "ADMIN"})
    public void MerchantController_UpdateMerchantPatch_ShouldUpdate() throws Exception {
        when(merchantService.updateMerchantPatch(anyString(), any(MerchantDto.class))).thenReturn(merchantDto);

        MvcResult mvcResult = mockMvc.perform(patch("/api/merchants/{id}", publicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(merchantUpdateRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        MerchantUpdateResponseDto merchantUpdateResponseDto1 = objectMapper.readValue(jsonResponse, MerchantUpdateResponseDto.class);

        assertThat(merchantUpdateResponseDto1).isEqualTo(merchantUpdateResponseDto);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"MERCHANT", "ADMIN"})
    public void MerchantController_UpdateMerchantPatch_ShouldReturnNotFoundStatus_IfMerchantWithIdNotExist() throws Exception {
        when(merchantService.updateMerchantPatch(anyString(), any(MerchantDto.class))).thenThrow(new MerchantNotFoundException(publicId));

        mockMvc.perform(patch("/api/merchants/{id}", publicId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(merchantUpdateRequestDto))
        ).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"MERCHANT", "ADMIN"})
    public void MerchantController_UpdateMerchantPatch_ShouldReturnBadRequestStatus_IfMerchantEnterInvalidData() throws Exception {
        MerchantPatchUpdateRequestDto invalidMerchantRequest = MerchantPatchUpdateRequestDto.builder()
                .phoneNumber("+")
                .build();

        mockMvc.perform(patch("/api/merchants/{id}", publicId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidMerchantRequest))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"MERCHANT", "ADMIN"})
    public void MerchantController_DeleteMerchant_ShouldDeleteMerchant() throws Exception {
        mockMvc.perform(delete("/api/merchants/{id}", publicId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles  = {"MERCHANT", "ADMIN"})
    public void MerchantController_DeleteMerchant_ShouldReturnNotFoundStatus_IfMerchantWithIdNotExist() throws Exception {
        doThrow(new MerchantNotFoundException(publicId)).when(merchantService).removeMerchantByPublicId(anyString());

        mockMvc.perform(delete("/api/merchants/{id}", publicId))
                .andExpect(status().isNotFound());
    }

    private List<MerchantDto> generateMerchants(int count) {
        List<MerchantDto> merchants = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            MerchantDto merchant = new MerchantDto();
            // Set user properties
            merchants.add(merchant);
        }
        return merchants;
    }


}
