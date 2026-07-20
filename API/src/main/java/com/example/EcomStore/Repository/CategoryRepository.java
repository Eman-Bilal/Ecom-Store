package com.example.EcomStore.Repository;

import com.example.EcomStore.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,String> {

  List<Category> findByActiveTrue();
  Optional<Category> findByIdAndActiveTrue(Long id);
  Optional<Category> findByIdAndActiveFalse(Long id);
  @Query("SELECT oi.product.id, SUM(oi.quantity) as totalSold FROM OrderItems oi GROUP BY oi.product.id ORDER BY totalSold DESC")
  List<Object[]> findBestSellingProductIds();

}