package com.example.EcomStore.Repository;

import com.example.EcomStore.Entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, String> {

}
