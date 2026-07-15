package com.example.EcomStore.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {

  @NotBlank(message = "Product Id is required")
  private String productId;

  @Min(value = 1, message = "Quantity must be at least 1")
  private int quantity;
}