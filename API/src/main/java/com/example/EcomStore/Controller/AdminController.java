package com.example.EcomStore.Controller;

import com.example.EcomStore.Dto.AdminResponse;
import com.example.EcomStore.Dto.RegisterRequest;
import com.example.EcomStore.Entities.Admin;
import com.example.EcomStore.Entities.CustomerOrder;
import com.example.EcomStore.Entities.OrderStatus;
import com.example.EcomStore.Service.AdminService;
import com.example.EcomStore.Service.CustomerOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admins")

public class AdminController {

  private final AdminService adminService;
  private final CustomerOrderService customerOrderService;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public List<AdminResponse> getAll() {
    return adminService.getAll().stream()
        .map(AdminResponse::fromEntity)
        .toList();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/register")
  public ResponseEntity<Admin> register(@Valid @RequestBody RegisterRequest admin) {
    Admin saved = adminService.register(admin);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("{id}/status")
  public ResponseEntity<CustomerOrder> updateStatus(@PathVariable String id, @RequestParam OrderStatus status){
    return ResponseEntity.ok(customerOrderService.updateOrderStatus(id, status));
  }
}
