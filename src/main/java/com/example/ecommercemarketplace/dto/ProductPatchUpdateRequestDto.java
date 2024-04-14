package com.example.ecommercemarketplace.dto;

import com.example.ecommercemarketplace.models.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPatchUpdateRequestDto {

    private String name;

    private Long categoryId;

    @Min(value = 0 ,message = "Price should be greater than 0")
    private BigDecimal price;

    @Min(value = 0, message = "Quantity of product should be positive")
    private Long inventory;

    private String description;

}
