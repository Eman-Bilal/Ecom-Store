package com.example.EcomStore.Controller;

import com.example.EcomStore.Dto.RegisterRequest;
import com.example.EcomStore.Entities.Admin;
import com.example.EcomStore.Service.AdminService;
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

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public List<Admin> getAll(){
    return adminService.getAll();
  }

  @PostMapping("/register")
  public ResponseEntity<Admin> register(@Valid @RequestBody RegisterRequest admin) {
    Admin saved = adminService.register(admin);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }


}
