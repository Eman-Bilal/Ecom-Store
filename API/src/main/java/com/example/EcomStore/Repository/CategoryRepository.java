package com.example.EcomStore.Repository;

import com.example.EcomStore.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,String> {

  List<Category> findByActiveTrue();
  Optional<Category> findByIdAndActiveTrue(Long id);
  Optional<Category> findByIdAndActiveFalse(Long id);
}