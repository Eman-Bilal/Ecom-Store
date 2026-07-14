package com.example.EcomStore.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(length = 36, updatable = false, nullable = false)
  private String id;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @NotBlank(message = "Product name is required")
  @Column(nullable = false)
  private String name;

  private String description;

//  @Column(nullable = false, comment = "active is missing from request body")
  private boolean active = true;

  @DecimalMin(value = "0.0", inclusive = false)
  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Min(value = 0)
  @Column(nullable = false)
  private int quantityInStock;

  @Enumerated(EnumType.STRING)
  private ProductStatus status;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;
}