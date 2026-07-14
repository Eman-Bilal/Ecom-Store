package com.example.EcomStore.Repository;

import com.example.EcomStore.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,String> {

}
