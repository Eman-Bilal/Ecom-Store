package com.example.EcomStore.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems {
  @Id
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  private CustomerOrder order;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(nullable = false)
  private String productName;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private BigDecimal unitPrice;

  @Column(nullable = false)
  private BigDecimal lineTotal;
}