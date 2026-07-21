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
  private final EmailService emailService;

  public Category createCategory(Category category) {
    Category saved = categoryRepository.save(category);
    emailService.sendAdminNotification("Category created",
        "A new category was created: " + saved.getCategoryName());
    return saved;
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
    Category saved= categoryRepository.save(existing);
    emailService.sendAdminNotification("Category updated",
        "A category with id and name: "
        + id+" ,"+ existing.getCategoryName() +" was updated" );
    return saved;
  }

  public void deleteCategory(Long id) {
    Category category = getById(id);
    category.setActive(false);
    categoryRepository.save(category);

    List<Product> products = productRepository.findByCategoryId(id);
    products.forEach(p -> p.setActive(false));
    productRepository.saveAll(products);
    emailService.sendAdminNotification("Category Deleted",
        "A category with id and name: "+id+" ,"+ category.getCategoryName()+ " was deleted ");

  }

  public Category reactivateCategory(Long id) {
    Category category = categoryRepository.findByIdAndActiveFalse(id)
        .orElseThrow(() -> new ResourceNotFoundException("Inactive category not found with id: " + id));
    category.setActive(true);
    categoryRepository.save(category);

    List<Product> products = productRepository.findByCategoryId(id);
    products.forEach(p -> p.setActive(true));
    productRepository.saveAll(products);
    emailService.sendAdminNotification("Category Reactivated","A category with id and name: "
        +id+" ,"+ category.getCategoryName()+"was reactivated ");

    return category;
  }
}