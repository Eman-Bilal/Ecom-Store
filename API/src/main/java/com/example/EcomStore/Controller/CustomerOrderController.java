package com.example.EcomStore.Controller;

import com.example.EcomStore.Entities.CustomerOrder;
import com.example.EcomStore.Service.CustomerOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders/")
public class CustomerOrderController {

  private final CustomerOrderService customerOrderService;

  public CustomerOrderController(CustomerOrderService customerOrderService) {
    this.customerOrderService = customerOrderService;
  }

  @PostMapping("create")
  public ResponseEntity<CustomerOrder> createOrder(
      @Valid @RequestBody CustomerOrder order) {

    CustomerOrder savedOrder = customerOrderService.createOrder(order);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
  }
}