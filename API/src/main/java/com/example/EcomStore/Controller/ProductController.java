package com.example.EcomStore.Controller;

import com.example.EcomStore.Dto.ProductRequestDto;
import com.example.EcomStore.Dto.ProductResponseDto;
import com.example.EcomStore.Entities.Product;
import com.example.EcomStore.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;

//  @GetMapping
//  public ResponseEntity<List<Product>> getAll() {
//    return ResponseEntity.ok(productService.getAll());
//  }


  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}/{categoryId}")
  public ResponseEntity<Product> update(@PathVariable String id,
                                        @Valid @RequestBody Product product,
                                        @PathVariable long categoryId) {
    return ResponseEntity.ok(productService.updateProduct(id, product, categoryId));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable String id) {
    productService.deleteProduct(id);
    return ResponseEntity.ok("Product safely inactivated");
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}")
  public ResponseEntity<Product> reactivate(@PathVariable String id) {
    return ResponseEntity.ok(productService.reactivateProduct(id));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/{categoryId}")
  public ResponseEntity<Product> create(@Valid @RequestBody Product product, @PathVariable long categoryId) {
    Product saved = productService.createProduct(categoryId, product);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @GetMapping("/category/{categoryId}")
  public ResponseEntity<List<ProductResponseDto>> getByCategory(@PathVariable Long categoryId) {
    return ResponseEntity.ok(productService.getByCategory(categoryId));
  }

  @GetMapping
  public ResponseEntity<List<ProductResponseDto>> getAll() {
    return ResponseEntity.ok(productService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDto> getById(@PathVariable String id) {
    return ResponseEntity.ok(productService.getResponseById(id));
  }

  @GetMapping("/search")
  public ResponseEntity<List<ProductResponseDto>> search(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice,
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) String sortBy) {
    return ResponseEntity.ok(productService.searchProducts(name, minPrice, maxPrice, categoryId, sortBy));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping(value = "/category/{categoryId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Product> createWithImage(
      @PathVariable Long categoryId,
      @RequestPart("product") ProductRequestDto product,
      @RequestPart("file") MultipartFile file) {
    Product saved = productService.createProductWithImage(categoryId, product, file);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Product> updateImage(@PathVariable String id, @RequestPart("file") MultipartFile file) {
    return ResponseEntity.ok(productService.updateProductImage(id, file));
  }

}