package com.example.EcomStore.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(length = 36, updatable = false, nullable = false)
  private String id;

  @NotBlank(message = "Name is required")
  @Column(nullable = false)
  private String firstName;

  @NotBlank(message = "Name is required")
  @Column(nullable = false)
  private String lastName;

  @Email(message = "Invalid Email")
  @NotBlank(message = "Email is required")
  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  @NotBlank(message = "Password is required")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
      message = "Password must be at least 6 characters and contain an uppercase letter, lowercase letter, number, and special character."
  )
  private String password;

  @Column(nullable = false, unique = true)
  @Pattern(regexp = "^03\\d{9}$", message = "Phone number must be exactly 11 digits")
  private String phone;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
