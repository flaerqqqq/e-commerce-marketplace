package com.example.ecommercemarketplace.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {

    private Long id;

    private String name;

    private Long categoryId;

    private Long merchantId;

    private BigDecimal price;

    private Long inventory;

    private String description;

    private MainProductImageResponseDto mainProductImage;

    private List<ProductImageResponseDto> productImages;
}
