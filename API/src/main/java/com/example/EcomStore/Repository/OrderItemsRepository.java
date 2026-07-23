package com.example.EcomStore.Repository;

import com.example.EcomStore.Entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItems, String> {

  @Query("""
    SELECT oi.productName, SUM(oi.quantity)
    FROM OrderItems oi
    WHERE oi.order.createdAt BETWEEN :start AND :end
    GROUP BY oi.productName
    ORDER BY SUM(oi.quantity) DESC
    """)
  List<Object[]> findTopSellingProducts(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
  List<OrderItems> findByOrder_Id(String orderId);
}
