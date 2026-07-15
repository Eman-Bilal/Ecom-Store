package com.example.EcomStore.Dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class CreateOrderRequest {

  @NotBlank(message = "First name is required")
  private String firstName;

  @NotBlank(message = "Last name is required")
  private String lastName;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email")
  private String email;

  @NotBlank(message = "Phone is required")
  @Pattern(regexp = "^03\\d{9}$", message = "Phone number must be exactly 11 digits")
  private String phone;

  @NotBlank(message = "Shipping Address is required")
  private String shippingAddress;

  @NotBlank(message = "City name is required")
  private String city;

  private String postalCode;

  @NotEmpty(message = "Order must contain at least one item")
  @Valid
  private List<OrderItemRequest> items;

}
