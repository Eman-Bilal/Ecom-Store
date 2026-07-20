package com.example.EcomStore.Controller;

import com.example.EcomStore.Entities.Admin;
import com.example.EcomStore.Service.JwtService;
import com.example.EcomStore.Service.AdminService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AdminService adminService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
    Admin admin = adminService.findByEmail(request.getEmail());

    if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
      throw new BadCredentialsException("Invalid email or password");
    }

    String token = jwtService.generateToken(admin.getEmail(), admin.getRole().name());
    return ResponseEntity.ok(Map.of("token", token));
  }

  @Getter
  @Setter
  public static class LoginRequest {
    private String email;
    private String password;
  }
}