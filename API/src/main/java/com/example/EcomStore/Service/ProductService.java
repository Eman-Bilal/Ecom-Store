package com.example.EcomStore.Service;

import com.example.EcomStore.Entities.Category;
import com.example.EcomStore.Entities.Product;
import com.example.EcomStore.Entities.ProductStatus;
import com.example.EcomStore.Exception.ResourceNotFoundException;
import com.example.EcomStore.Repository.CategoryRepository;
import com.example.EcomStore.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

  void syncStatus(Product product) {
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

  public List<Product> searchProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, Long categoryId, String sortBy ){
    List<Product> products = productRepository.findByActiveTrue();

    List<Product> filtered = products.stream()
        .filter(p -> name == null || p.getName().toLowerCase().contains(name.toLowerCase()))
        .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
        .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
        .filter(p -> categoryId == null || p.getCategory().getId().equals(categoryId))
        .collect(Collectors.toCollection(ArrayList::new));

    if ("price_asc".equals(sortBy)) {
      filtered.sort(Comparator.comparing(Product::getPrice));
    } else if ("price_desc".equals(sortBy)) {
      filtered.sort(Comparator.comparing(Product::getPrice).reversed());
    } else if ("newest".equals(sortBy)) {
      filtered.sort(Comparator.comparing(Product::getCreatedAt).reversed());
    }
    return filtered;
  }
}