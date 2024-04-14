package com.example.ecommercemarketplace.dto;

import com.example.ecommercemarketplace.models.Category;
import com.example.ecommercemarketplace.models.Merchant;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateRequestDto {

    @NotBlank(message = "Name field is required")
    private String name;

    @NotNull(message = "Category field is required")
    private Long categoryId;

    @Min(value = 0 ,message = "Price should be greater than 0")
    @NotNull(message = "Price field is required")
    private BigDecimal price;

    @Min(value = 0, message = "Quantity of product should be positive")
    @NotNull(message = "Inventory field is required")
    private Long inventory;

    @NotBlank(message = "Description field is required")
    private String description;

}
