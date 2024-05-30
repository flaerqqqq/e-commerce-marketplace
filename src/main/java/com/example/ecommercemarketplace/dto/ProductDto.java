package com.example.ecommercemarketplace.dto;


import com.example.ecommercemarketplace.models.Category;
import com.example.ecommercemarketplace.models.Merchant;
import jakarta.persistence.*;
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
public class ProductDto {

    private Long id;

    private String name;

    private Category category;

    private BigDecimal price;

    private Long inventory;

    private String description;

    private Merchant merchant;

    private MainProductImageDto mainProductImage;

    private List<ProductImageDto> productImages;
}
