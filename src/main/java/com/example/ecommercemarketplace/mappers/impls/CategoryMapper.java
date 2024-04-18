package com.example.ecommercemarketplace.mappers.impls;

import com.example.ecommercemarketplace.dto.CategoryDto;
import com.example.ecommercemarketplace.exceptions.CategoryNotFoundException;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.Category;
import com.example.ecommercemarketplace.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryMapper implements Mapper<Category, CategoryDto> {

    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public Category mapFrom(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    @Override
    public CategoryDto mapTo(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }
}
