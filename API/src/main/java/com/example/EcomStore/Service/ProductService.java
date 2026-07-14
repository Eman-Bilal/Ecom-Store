package com.example.EcomStore.Service;

import com.example.EcomStore.Entities.Category;
import com.example.EcomStore.Entities.Product;
import com.example.EcomStore.Entities.ProductStatus;
import com.example.EcomStore.Exception.ResourceNotFoundException;
import com.example.EcomStore.Repository.CategoryRepository;
import com.example.EcomStore.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public Product createProduct(long categoryId, Product product) {
    Category category = categoryRepository.findByIdAndActiveTrue(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    product.setCategory(category);
    syncStatus(product);
    return productRepository.save(product);
  }

  public List<Product> getByCategory(Long categoryId) {
    categoryRepository.findByIdAndActiveTrue(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    return productRepository.findByCategoryIdAndActiveTrue(categoryId);
  }

  public List<Product> getAll() {
    return productRepository.findByActiveTrue();
  }

  public Product getById(String id) {
    return productRepository.findByIdAndActiveTrue(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
  }

  public Product updateProduct(String id, Product updatedProduct, long newCategoryId) {
    Product existing = getById(id);

    existing.setName(updatedProduct.getName());
    existing.setDescription(updatedProduct.getDescription());
    existing.setPrice(updatedProduct.getPrice());
    existing.setQuantityInStock(updatedProduct.getQuantityInStock());
//    existing.setStatus(updatedProduct.getStatus());

    if (!existing.getCategory().getId().equals(newCategoryId)) {
      Category newCategory = categoryRepository.findByIdAndActiveTrue(newCategoryId)
          .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + newCategoryId));
      existing.setCategory(newCategory);
    }
    syncStatus(existing);
    return productRepository.save(existing);
  }

  private void syncStatus(Product product) {
    if (product.getQuantityInStock() <= 0) {
      product.setStatus(ProductStatus.OUT_OF_STOCK);
    } else {
      product.setStatus(ProductStatus.IN_STOCK);
    }
  }

  public void deleteProduct(String id) {
    Product product = getById(id);
    product.setActive(false);
    productRepository.save(product);
  }

  public Product reactivateProduct(String id) {
    Product product = productRepository.findByIdAndActiveFalse(id)
        .orElseThrow(() -> new ResourceNotFoundException("Inactive product not found with id: " + id));
    product.setActive(true);
    return productRepository.save(product);
  }
}