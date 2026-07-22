package com.example.EcomStore.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopProductDto {
  private String productName;
  private long unitsSold;
}