package com.example.EcomStore.Controller;

import com.example.EcomStore.Entities.Admin;
import com.example.EcomStore.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("admin/")

public class AdminController {

  private final AdminService adminService;

  @GetMapping("getAll")
  public List<Admin> getAll(){
    return adminService.getAll();
  }

  @PostMapping("/register")
  public ResponseEntity<Admin> register(@RequestBody Admin admin) {
    Admin saved = adminService.register(admin);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }


}
