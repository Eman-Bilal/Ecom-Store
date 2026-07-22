package com.example.EcomStore.Dto;

import com.example.EcomStore.Entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;


@Getter
@AllArgsConstructor
public class RecentOrderDto {
  private String orderNumber;
  private String customerName;
  private BigDecimal totalAmount;
  private OrderStatus orderStatus;
}
