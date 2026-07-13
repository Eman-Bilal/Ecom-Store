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
  private Long id;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @NotBlank(message = "Product name is required")
  @Column(nullable = false)
  private String name;

  private String description;

  @DecimalMin(value = "0.0", inclusive = false)
  @Column(nullable = false)
  private BigDecimal price;

  @Min(value = 0)
  @Column(nullable = false)
  private int quantityInStock;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ProductStatus status;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;
}