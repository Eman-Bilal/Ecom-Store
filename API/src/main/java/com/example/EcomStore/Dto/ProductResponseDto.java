package com.example.EcomStore.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponseDto {

  private String id;
  private String name;
  private String description;
  private BigDecimal price;
  private int quantityInStock;

  private byte[] image;
}