package com.example.EcomStore.Controller;

import com.example.EcomStore.Dto.CreateOrderRequest;
import com.example.EcomStore.Entities.CustomerOrder;
import com.example.EcomStore.Service.CustomerOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")
public class CustomerOrderController {

  private final CustomerOrderService customerOrderService;

  public CustomerOrderController(CustomerOrderService customerOrderService) {
    this.customerOrderService = customerOrderService;
  }

  @PostMapping("/checkout")
  public ResponseEntity<CustomerOrder> checkout(@Valid @RequestBody CreateOrderRequest request) {
    CustomerOrder savedOrder = customerOrderService.createOrder(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerOrder> getById(@PathVariable String id) {
    return ResponseEntity.ok(customerOrderService.getById(id));
  }
}