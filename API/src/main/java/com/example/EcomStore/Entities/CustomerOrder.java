package com.example.EcomStore.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "customer_order")
public class CustomerOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(length = 36, updatable = false, nullable = false)
  private String id;

  @Column(nullable = false, unique = true)
  private String orderNumber;

  @NotBlank(message = "First name is required")
  @Column(nullable = false)
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Column(nullable = false)
  private String lastName;

  @Email(message = "Invalid email")
  @NotBlank(message = "Email is required")
  @Column(nullable = false)
  private String email;

  @NotBlank(message = "Phone number is required")
  @Column(nullable = false)
  @Pattern(regexp = "^03\\d{9}$", message = "Phone number must be exactly 11 digits")
  private String phone;

  @NotBlank(message = "Shipping address is required")
  @Column(nullable = false, columnDefinition = "TEXT")
  private String shippingAddress;

  @NotBlank(message = "City is required")
  @Column(nullable = false)
  private String city;

//  optional
  private String postalCode;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus orderStatus;

  @DecimalMin(value = "0.0")
  @Column(nullable = false,precision = 10, scale = 2)
  private BigDecimal subtotal;

  @DecimalMin(value = "0.0")
  @Column(nullable = false,precision = 10, scale = 2)
  private BigDecimal shippingFee;

  @DecimalMin(value = "0.0")
  @Column(nullable = false,precision = 10, scale = 2)
  private BigDecimal totalAmount;

  @Column(nullable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;
}