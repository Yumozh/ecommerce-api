package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.repository.CategoryRepository;
import org.yearup.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories()
    {
        return categoryRepository.findAll();
    }

    public Optional<Category> getById(int categoryId)
    {
        return categoryRepository.findById(categoryId);
    }

    public Category createCategory(Category category)
    {
        return categoryRepository.save(category);
    }

    public Optional<Category> updateCategory(int categoryId, Category updatedCategory)
    {
        return categoryRepository.findById(categoryId).map(existing -> {
            existing.setName(updatedCategory.getName());
            existing.setDescription(updatedCategory.getDescription());
            return categoryRepository.save(existing);
        });
    }

    public boolean deleteCategory(int categoryId)
    {
        if(categoryRepository.existsById(categoryId)){
            categoryRepository.deleteById(categoryId);
            return true;
        }
        return false;
    }
}
