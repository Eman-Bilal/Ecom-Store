package com.example.EcomStore.Dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

  @NotBlank(message = "First name is required")
  private String firstName;

  @NotBlank(message = "Last name is required")
  private String lastName;

  @Email(message = "Invalid email")
  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Password is required")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
      message = "Password must be at least 6 characters and contain an uppercase letter, lowercase letter, number, and special character."
  )
  private String password;

  @NotBlank(message = "Phone number is required")
  @Pattern(regexp = "^03\\d{9}$", message = "Phone number must be exactly 11 digits")
  private String phone;
}