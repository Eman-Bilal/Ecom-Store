package com.example.EcomStore.Repository;

import com.example.EcomStore.Entities.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, String> {
  long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
  Optional<CustomerOrder> findByOrderNumber(String orderNumber);

}
