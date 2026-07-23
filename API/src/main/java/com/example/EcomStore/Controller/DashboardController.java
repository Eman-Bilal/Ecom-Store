package com.example.EcomStore.Controller;

import com.example.EcomStore.Dto.DashboardResponse;
import com.example.EcomStore.Service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

  private final DashboardService dashboardService;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<DashboardResponse> getDashboard() {
    return ResponseEntity.ok(dashboardService.getDashboard());
  }
}