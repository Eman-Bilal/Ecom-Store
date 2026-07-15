package com.example.EcomStore.Controller;

import com.example.EcomStore.Entities.Category;
import com.example.EcomStore.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/categories/")
public class CategoryController {
  private final CategoryService categoryService;

  @PostMapping("/create")
  public ResponseEntity<Category> create(@Valid @RequestBody  Category category){
    Category saved= categoryService.createCategory(category);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @GetMapping("/getAll")
  public ResponseEntity<List<Category>> getAllCategories(){
    return ResponseEntity.ok(categoryService.getAll());
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<String > update(@PathVariable long id,@Valid @RequestBody Category category){
    categoryService.updateCategory(id, category);
    return ResponseEntity.ok("Record updated Successfully");
  }
  @GetMapping("/{id}")
  public ResponseEntity<Category> getCategoryById(@PathVariable long id){
    return ResponseEntity.ok(categoryService.getById(id));
  }
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteCategory(@PathVariable long id){
    categoryService.deleteCategory(id);
    return ResponseEntity.ok("Category safely inactivated");
  }

  @PatchMapping("/reactivate/{id}")
  public ResponseEntity<Category> reactivate(@PathVariable Long id) {
    return ResponseEntity.ok(categoryService.reactivateCategory(id));
  }
}

