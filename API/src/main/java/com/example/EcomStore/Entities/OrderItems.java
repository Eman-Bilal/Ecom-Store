package com.example.EcomStore.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderItems {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(length = 36, updatable = false, nullable = false)
  private String id;

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

  @Column(nullable = false,precision = 10, scale = 2)
  @DecimalMin("0.0")
  private BigDecimal unitPrice;

  @Column(nullable = false,precision = 10, scale = 2)
  private BigDecimal lineTotal;
}