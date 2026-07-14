package com.example.EcomStore.Repository;

import com.example.EcomStore.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,String> {
  List<Product> findByCategoryId(Long categoryId);
  List<Product> findByActiveTrue();
  Optional<Product> findByIdAndActiveTrue(String id);
  Optional<Product> findByIdAndActiveFalse(String id);
  List<Product> findByCategoryIdAndActiveTrue(Long categoryId);
}
