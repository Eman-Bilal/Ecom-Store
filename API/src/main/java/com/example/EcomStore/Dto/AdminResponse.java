package com.example.EcomStore.Dto;

import com.example.EcomStore.Entities.Admin;
import com.example.EcomStore.Entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminResponse {
  private String id;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private Role role;
  private LocalDateTime createdAt;

  public static AdminResponse fromEntity(Admin admin) {
    return new AdminResponse(
        admin.getId(),
        admin.getFirstName(),
        admin.getLastName(),
        admin.getEmail(),
        admin.getPhone(),
        admin.getRole(),
        admin.getCreatedAt()
    );
  }
}