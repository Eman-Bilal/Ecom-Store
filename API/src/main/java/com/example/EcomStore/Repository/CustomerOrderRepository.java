package com.example.EcomStore.Repository;

import com.example.EcomStore.Entities.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, String> {
  long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}
