package com.example.EcomStore.Controller;

import com.example.EcomStore.Entities.Category;
import com.example.EcomStore.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
  private final CategoryService categoryService;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping // create category
  public ResponseEntity<Category> create(@Valid @RequestBody  Category category){
    Category saved= categoryService.createCategory(category);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @GetMapping //get all categories
  public ResponseEntity<List<Category>> getAllCategories(){
    return ResponseEntity.ok(categoryService.getAll());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{id}")
  public ResponseEntity<Category> update(@PathVariable Long id, @Valid @RequestBody Category category){
    return ResponseEntity.ok(categoryService.updateCategory(id, category));
  }

  @GetMapping("/{id}") // get category through id
  public ResponseEntity<Category> getCategoryById(@PathVariable long id){
    return ResponseEntity.ok(categoryService.getById(id));
  }

  @DeleteMapping("/{id}") // delete using id
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> deleteCategory(@PathVariable long id){
    categoryService.deleteCategory(id);
    return ResponseEntity.ok("Category safely inactivated");
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}")
  public ResponseEntity<Category> reactivate(@PathVariable Long id) {
    return ResponseEntity.ok(categoryService.reactivateCategory(id));
  }
}

