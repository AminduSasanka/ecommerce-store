package com.ecommerece.store.service.category;

import com.ecommerece.store.exception.AlreadyExistException;
import com.ecommerece.store.exception.ResourceNotFoundException;
import com.ecommerece.store.model.Category;
import com.ecommerece.store.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category create(Category category) {
        return Optional.ofNullable(category)
                .filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() -> new AlreadyExistException(category.getName() + "already exists!"));
    }

    @Override
    public Category update(String categoryName, Long id) {
        return Optional.ofNullable(findById(id))
                .map(oldCategory -> {
                    oldCategory.setName(categoryName);
                    return categoryRepository.save(oldCategory);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public void delete(Long id) {
        categoryRepository
                .findById(id)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category does not exist!");
                });
    }
}
