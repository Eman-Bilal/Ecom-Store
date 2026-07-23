package com.example.EcomStore.Controller;

import com.example.EcomStore.Dto.CreateOrderRequest;
import com.example.EcomStore.Entities.CustomerOrder;
import com.example.EcomStore.Entities.OrderStatus;
import com.example.EcomStore.Exception.ResourceNotFoundException;
import com.example.EcomStore.Repository.CustomerOrderRepository;
import com.example.EcomStore.Service.CustomerOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class CustomerOrderController {

  private final CustomerOrderService customerOrderService;
  private final CustomerOrderRepository customerOrderRepository;

  public CustomerOrderController(CustomerOrderService customerOrderService, CustomerOrderRepository customerOrderRepository) {
    this.customerOrderService = customerOrderService;
    this.customerOrderRepository = customerOrderRepository;
  }

  @PostMapping("/checkout")
  public ResponseEntity<CustomerOrder> checkout(@Valid @RequestBody CreateOrderRequest request) {
    CustomerOrder savedOrder = customerOrderService.createOrder(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{id}")
  public ResponseEntity<CustomerOrder> getById(@PathVariable String id) {
    return ResponseEntity.ok(customerOrderService.getById(id));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/order-number/{orderNumber}")
  public CustomerOrder getByOrderNumberAdmin(@PathVariable String orderNumber) {
    return customerOrderRepository.findByOrderNumber(orderNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with order number: " + orderNumber));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<CustomerOrder>> getAll() {
    return ResponseEntity.ok(customerOrderService.getAll());
  }

  @GetMapping("/track")
  public ResponseEntity<CustomerOrder> trackOrder(
      @RequestParam String orderNumber,
      @RequestParam String email) {
    return ResponseEntity.ok(customerOrderService.getByOrderNumberAndEmail(orderNumber, email));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}/status")
  public ResponseEntity<CustomerOrder> updateStatus(
      @PathVariable String id,
      @RequestParam OrderStatus status) {
    return ResponseEntity.ok(customerOrderService.updateOrderStatus(id, status));
  }

  // Customer-facing
  @PatchMapping("/cancel")
  public ResponseEntity<CustomerOrder> cancelByCustomer(
      @RequestParam String orderNumber,
      @RequestParam String email) {
    return ResponseEntity.ok(customerOrderService.cancelOrderByCustomer(orderNumber, email));
  }

  // Admin-facing
  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}/cancel")
  public ResponseEntity<CustomerOrder> cancelByAdmin(@PathVariable String id) {
    return ResponseEntity.ok(customerOrderService.cancelOrderById(id));
  }
}