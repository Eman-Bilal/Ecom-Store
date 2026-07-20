package com.example.EcomStore.Controller;

import com.example.EcomStore.Entities.Product;
import com.example.EcomStore.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/{categoryId}")
  public ResponseEntity<Product> create(@Valid @RequestBody Product product, @PathVariable long categoryId) {
    Product saved = productService.createProduct(categoryId, product);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @GetMapping("/category/{categoryId}")
  public ResponseEntity<List<Product>> getByCategory(@PathVariable Long categoryId) {
    return ResponseEntity.ok(productService.getByCategory(categoryId));
  }

  @GetMapping
  public ResponseEntity<List<Product>> getAll() {
    return ResponseEntity.ok(productService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getById(@PathVariable String id) {
    return ResponseEntity.ok(productService.getById(id));
  }

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

  @GetMapping("/search")
  public ResponseEntity<List<Product>> search(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice,
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) String sortBy) {
    return ResponseEntity.ok(productService.searchProducts(name, minPrice, maxPrice, categoryId, sortBy));
  }

}