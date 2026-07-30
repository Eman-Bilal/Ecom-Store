package com.example.EcomStore.Service;

import com.example.EcomStore.Dto.ProductRequestDto;
import com.example.EcomStore.Dto.ProductResponseDto;
import com.example.EcomStore.Entities.Category;
import com.example.EcomStore.Entities.Product;
import com.example.EcomStore.Entities.ProductStatus;
import com.example.EcomStore.Exception.ResourceNotFoundException;
import com.example.EcomStore.Repository.CategoryRepository;
import com.example.EcomStore.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final EmailService emailService;
  private final FileStorageService fileStorageService;

  public Product createProduct(long categoryId, Product product) {
    Category category = categoryRepository.findByIdAndActiveTrue(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    product.setCategory(category);
    syncStatus(product);
    emailService.sendAdminNotification("A product is created",
        "A product was added in the category with id and name: " + categoryId + " ," + category.getCategoryName());
    return productRepository.save(product);
  }

  public Product createProductWithImage(Long categoryId, ProductRequestDto dto, MultipartFile file) {
    Product product = new Product();
    product.setName(dto.getName());
    product.setDescription(dto.getDescription());
    product.setPrice(dto.getPrice());
    product.setQuantityInStock(dto.getQuantityInStock());

    if (file != null && !file.isEmpty()) {
      FileStorageService.StoredFile stored = fileStorageService.storeFile(file);
      product.setImageUrl(stored.path());
      product.setImageContentType(stored.contentType());
    }

    return createProduct(categoryId, product);
  }

  public List<ProductResponseDto> getAllActive() {
    return productRepository.findByActiveTrue()
        .stream()
        .map(this::toDto)
        .toList();
  }

  public List<ProductResponseDto> getAll() {
    return productRepository.findAll()
        .stream()
        .map(this::toDto)
        .toList();
  }
  public List<ProductResponseDto> getByCategory(Long categoryId) {
    categoryRepository.findByIdAndActiveTrue(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    return productRepository.findByCategoryIdAndActiveTrue(categoryId)
        .stream()
        .map(this::toDto)
        .toList();
  }

  public ProductResponseDto getResponseById(String id) {
    return toDto(getById(id));
  }

  public List<ProductResponseDto> searchProducts(String name, BigDecimal minPrice, BigDecimal maxPrice,
                                                 Long categoryId, String sortBy) {
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

    return filtered.stream().map(this::toDto).toList();
  }

  // Internal use only (update/delete/reactivate need the entity, not the DTO)
  public Product getById(String id) {
    return productRepository.findByIdAndActiveTrue(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
  }

  private ProductResponseDto toDto(Product product) {
    byte[] image = null;
    if (product.getImageUrl() != null) {
      try {
        image = Files.readAllBytes(Paths.get(product.getImageUrl()));
      } catch (IOException e) {
        log.warn("Could not read image for product {}: {}", product.getId(), e.getMessage());
        // leave image null rather than failing the whole request
      }
    }
    return new ProductResponseDto(
        product.getId(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getQuantityInStock(),
        image,
        product.getImageContentType(),
        product.getCategory() != null ? product.getCategory().getCategoryName() : null,
        product.isActive()
    );
  }

  public Product updateProduct(String id, Product updatedProduct, long newCategoryId) {
    Product existing = getById(id);
    existing.setName(updatedProduct.getName());
    existing.setDescription(updatedProduct.getDescription());
    existing.setPrice(updatedProduct.getPrice());
    existing.setQuantityInStock(updatedProduct.getQuantityInStock());
    existing.setImageUrl(updatedProduct.getImageUrl());

    if (!existing.getCategory().getId().equals(newCategoryId)) {
      Category newCategory = categoryRepository.findByIdAndActiveTrue(newCategoryId)
          .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + newCategoryId));
      existing.setCategory(newCategory);
    }
    syncStatus(existing);
    emailService.sendAdminNotification("Product updated",
        "<br>A product was updated with id and name: <span>" + id + " </span>, " + updatedProduct.getName());
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
    emailService.sendAdminNotification("Product Deleted",
        "<br>A product with id and name: <span>" + id + "</span> ," + product.getName() + " was deleted");
  }

  public Product reactivateProduct(String id) {
    Product product = productRepository.findByIdAndActiveFalse(id)
        .orElseThrow(() -> new ResourceNotFoundException("Inactive product not found with id: " + id));
    product.setActive(true);
    emailService.sendAdminNotification("Product Reactivated",
        "<br>A product with id and name: <span>" + id + "</span> ," + product.getName() + " was reactivated");
    return productRepository.save(product);
  }

  public Product updateProductImage(String id, MultipartFile file) {
    Product existing = getById(id);
    FileStorageService.StoredFile stored = fileStorageService.storeFile(file);
    existing.setImageUrl(stored.path());
    existing.setImageContentType(stored.contentType());
    emailService.sendAdminNotification("Product image updated",
        "<br>Image updated for product with id: <span>" + id + " </span>");
    return productRepository.save(existing);
  }
}