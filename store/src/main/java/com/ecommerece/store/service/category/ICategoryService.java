package com.ecommerece.store.service.category;

import com.ecommerece.store.model.Category;

import java.util.List;

public interface ICategoryService {
    Category findByName(String name);
    Category findById(Long id);
    List<Category> findAll();
    Category create(Category category);
    Category update(String categoryName, Long id);
    void delete(Long id);
}
