package com.bookstore.service;

import com.bookstore.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> findAllCategories();

    Category findCategoryById(Long id);

    void deleteCategory(Long id);

    void saveCategory(Category category);
}
