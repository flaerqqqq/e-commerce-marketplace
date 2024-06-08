package com.example.ecommercemarketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReviewResponseDto {

    private Long id;

    private Long userId;

    private Long productId;

    private String textContent;

    private List<ProductReviewMediaContentResponseDto> mediaContents;
}
