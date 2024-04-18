package com.example.ecommercemarketplace.services.impls;

import com.example.ecommercemarketplace.dto.CategoryDto;
import com.example.ecommercemarketplace.exceptions.CategoryNotFoundException;
import com.example.ecommercemarketplace.mappers.Mapper;
import com.example.ecommercemarketplace.models.Category;
import com.example.ecommercemarketplace.repositories.CategoryRepository;
import com.example.ecommercemarketplace.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final Mapper<Category, CategoryDto> categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category savedCategory = categoryRepository.save(categoryMapper.mapFrom(categoryDto));

        return categoryMapper.mapTo(savedCategory);
    }

    @Override
    public CategoryDto updateCategoryFully(Long id, CategoryDto categoryDto) {
        throwIfCategoryNotFoundById(id);

        Category category = categoryMapper.mapFrom(categoryDto);
        category.setId(id);

        Category updateCategory = categoryRepository.save(category);

        return categoryMapper.mapTo(updateCategory);
    }

    @Override
    public CategoryDto updateCategoryPatch(Long id, CategoryDto categoryDto) {
        throwIfCategoryNotFoundById(id);

        Category category = categoryRepository.findById(id).map((categ) -> {
            Optional.ofNullable(categoryDto.getName()).ifPresent(categ::setCategoryName);
            Optional.ofNullable(categoryDto.getDescription()).ifPresent(categ::setDescription);
            return categ;
        }).get();

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.mapTo(savedCategory);
    }

    @Override
    public Page<CategoryDto> findAllCategories(Pageable pageable) {
        Page<Category> pageOfCategories = categoryRepository.findAll(pageable);

        return pageOfCategories.map(categoryMapper::mapTo);
    }

    @Override
    public void deleteCategory(Long id) {
        throwIfCategoryNotFoundById(id);

        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new CategoryNotFoundException("Category with id=%d is not found".formatted(id)));

        return categoryMapper.mapTo(category);
    }

    private void throwIfCategoryNotFoundById(Long id){
        if (!categoryRepository.existsById(id)){
            throw new CategoryNotFoundException("Category with id=%d is not found".formatted(id));
        }
    }
}
