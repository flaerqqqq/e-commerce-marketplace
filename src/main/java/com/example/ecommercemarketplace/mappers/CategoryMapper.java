package com.example.ecommercemarketplace.mappers;

import com.example.ecommercemarketplace.dto.CategoryDto;
import com.example.ecommercemarketplace.models.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {

    CategoryDto mapTo(Category category);

    Category mapFrom(CategoryDto categoryDto);
}
