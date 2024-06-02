package com.example.ecommercemarketplace.dto;

import com.example.ecommercemarketplace.models.ProductReviewMediaContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReviewDto {

    private Long id;

    private Long userId;

    private Long productId;

    private String textContent;

    private List<ProductReviewMediaContentDto> mediaContents;
}
