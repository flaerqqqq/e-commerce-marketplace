package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.CategoryDto;
import com.example.ecommercemarketplace.exceptions.CategoryNotFoundException;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.Category;
import com.example.ecommercemarketplace.repositories.CategoryRepository;
import com.example.ecommercemarketplace.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final Mapper<Category, CategoryDto> categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return null;
    }

    @Override
    public CategoryDto updateCategoryFully(Long id, CategoryDto categoryDto) {
        return null;
    }

    @Override
    public CategoryDto updateCategoryPatch(Long id, CategoryDto categoryDto) {
        return null;
    }

    @Override
    public void deleteCategory(Long id) {

    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new CategoryNotFoundException("Category with id=%d is not found".formatted(id)));

        return categoryMapper.mapTo(category);
    }
}
