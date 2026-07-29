package com.example.EcomStore.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderItemDetailDto {

  private String productName;
  private int quantity;
  private BigDecimal unitPrice;
  private BigDecimal lineTotal;
}