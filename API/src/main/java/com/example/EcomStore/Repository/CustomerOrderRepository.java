package com.example.EcomStore.Repository;

import com.example.EcomStore.Entities.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, String> {
  long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
  Optional<CustomerOrder> findByOrderNumber(String orderNumber);

  @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM CustomerOrder o WHERE o.createdAt BETWEEN :start AND :end")
  BigDecimal sumRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
  List<CustomerOrder> findTop10ByOrderByCreatedAtDesc();

}
