package com.example.EcomStore.Dto;

import com.example.EcomStore.Entities.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderInvoiceDto {
  private String orderNumber;
  private String customerName;
  private String email;
  private String phone;
  private String shippingAddress;
  private String city;
  private String postalCode;
  private OrderStatus orderStatus;
  private List<OrderItemDetailDto> items;
  private BigDecimal subtotal;
  private BigDecimal shippingFee;
  private BigDecimal totalAmount;
  private LocalDateTime placedAt;
}