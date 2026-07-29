package com.example.EcomStore.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ProductRequestDto {
  @NotBlank
  private String name;

  private String description;

  @DecimalMin("0.0")
  private BigDecimal price;

  @Min(0)
  private int quantityInStock;
}
