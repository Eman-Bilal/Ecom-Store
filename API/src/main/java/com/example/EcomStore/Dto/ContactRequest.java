package com.example.EcomStore.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactRequest {

  @NotBlank(message = "Name is required")
  private String name;

  @Email(message = "Invalid email")
  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Subject is required")
  private String subject;

  @NotBlank(message = "Message is required")
  private String message;
}