package com.example.ecommercemarketplace.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReviewRequestDto {

    @NotBlank(message = "Text content shouldn't be empty")
    private String textContent;
}
