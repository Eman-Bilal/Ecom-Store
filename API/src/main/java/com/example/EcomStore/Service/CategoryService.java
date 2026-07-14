package com.example.EcomStore.Service;

import com.example.EcomStore.Entities.Category;
import com.example.EcomStore.Entities.Product;
import com.example.EcomStore.Exception.ResourceNotFoundException;
import com.example.EcomStore.Repository.CategoryRepository;
import com.example.EcomStore.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  public Category createCategory(Category category) {
    return categoryRepository.save(category);
  }

  public List<Category> getAll() {
    return categoryRepository.findByActiveTrue();
  }

  public Category getById(Long id) {
    return categoryRepository.findByIdAndActiveTrue(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
  }

  public Category updateCategory(Long id, Category updatedCategory) {
    Category existing = getById(id);
    existing.setCategoryName(updatedCategory.getCategoryName());
    existing.setDescription(updatedCategory.getDescription());
    return categoryRepository.save(existing);
  }
    public void deleteCategory(Long id) {
      Category category = getById(id);
      category.setActive(false);
      categoryRepository.save(category);

      List<Product> products = productRepository.findByCategoryId(id);
      products.forEach(p -> p.setActive(false));
      productRepository.saveAll(products);
    }

    public Category reactivateCategory(Long id) {
      Category category = categoryRepository.findByIdAndActiveFalse(id)
          .orElseThrow(() -> new ResourceNotFoundException("Inactive category not found with id: " + id));
      category.setActive(true);
      categoryRepository.save(category);

      List<Product> products = productRepository.findByCategoryId(id);
      products.forEach(p -> p.setActive(true));
      productRepository.saveAll(products);

      return category;
    }
  }