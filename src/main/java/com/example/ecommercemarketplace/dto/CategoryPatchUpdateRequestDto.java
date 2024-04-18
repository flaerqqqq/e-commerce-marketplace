package com.example.ecommercemarketplace.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryPatchUpdateRequestDto {

    private String name;

    private String description;
}
