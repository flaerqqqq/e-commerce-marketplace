package com.example.ecommercemarketplace.dto;


import com.example.ecommercemarketplace.models.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
}
