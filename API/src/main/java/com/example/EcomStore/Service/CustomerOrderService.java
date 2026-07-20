package com.example.EcomStore.Service;

import com.example.EcomStore.Dto.CreateOrderRequest;
import com.example.EcomStore.Dto.OrderItemRequest;
import com.example.EcomStore.Entities.*;
import com.example.EcomStore.Exception.InsufficientStockException;
import com.example.EcomStore.Exception.ResourceNotFoundException;
import com.example.EcomStore.Repository.CustomerOrderRepository;
import com.example.EcomStore.Repository.OrderItemsRepository;
import com.example.EcomStore.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerOrderService {

  private final CustomerOrderRepository customerOrderRepository;
  private final OrderItemsRepository orderItemsRepository;
  private final ProductRepository productRepository;
  private final ProductService productService;

  private String generateOrderNumber() {
    LocalDate today = LocalDate.now();
    LocalDateTime startOfDay = today.atStartOfDay();
    LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

    long orderCount = customerOrderRepository.countByCreatedAtBetween(startOfDay, endOfDay);
    String date = today.format(DateTimeFormatter.BASIC_ISO_DATE);
    return String.format("ORD-%s-%04d", date, orderCount + 1);
  }

  @Transactional
  public CustomerOrder createOrder(CreateOrderRequest request) {

    List<OrderItems> orderItemsList = new ArrayList<>();
    BigDecimal subtotal = BigDecimal.ZERO;

    // Build order first (needed to link items), save later once totals are known
    CustomerOrder order = new CustomerOrder();
    order.setFirstName(request.getFirstName());
    order.setLastName(request.getLastName());
    order.setEmail(request.getEmail());
    order.setPhone(request.getPhone());
    order.setShippingAddress(request.getShippingAddress());
    order.setCity(request.getCity());
    order.setPostalCode(request.getPostalCode());
    order.setOrderStatus(OrderStatus.PENDING);
    order.setOrderNumber(generateOrderNumber());

    for (OrderItemRequest itemReq : request.getItems()) {
      Product product = productRepository.findByIdAndActiveTrue(itemReq.getProductId())
          .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemReq.getProductId()));

      if (product.getQuantityInStock() < itemReq.getQuantity()) {
        throw new InsufficientStockException(
            "Insufficient stock for " + product.getName() + ". Available: " + product.getQuantityInStock());
      }

      // Decrement stock, keep status in sync
      product.setQuantityInStock(product.getQuantityInStock() - itemReq.getQuantity());
      productService.syncStatus(product);
      productRepository.save(product);

      BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
      subtotal = subtotal.add(lineTotal);

      OrderItems orderItem = new OrderItems();
      orderItem.setOrder(order);
      orderItem.setProduct(product);
      orderItem.setProductName(product.getName());
      orderItem.setQuantity(itemReq.getQuantity());
      orderItem.setUnitPrice(product.getPrice());
      orderItem.setLineTotal(lineTotal);

      orderItemsList.add(orderItem);
    }

    BigDecimal shippingFee = BigDecimal.valueOf(200); // flat fee for now
    BigDecimal totalAmount = subtotal.add(shippingFee);

    order.setSubtotal(subtotal);
    order.setShippingFee(shippingFee);
    order.setTotalAmount(totalAmount);

    CustomerOrder savedOrder = customerOrderRepository.save(order);
    orderItemsList.forEach(item -> item.setOrder(savedOrder));
    orderItemsRepository.saveAll(orderItemsList);

    return savedOrder;
  }

  public CustomerOrder getById(String id) {
    return customerOrderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
  }

  public List<CustomerOrder> getAll() {
    return customerOrderRepository.findAll();
  }

//Track order for customer
//  public CustomerOrder getByOrderNumberAndEmail(String orderNumber, String email) {
//    CustomerOrder order = customerOrderRepository.findByOrderNumber(orderNumber)
//        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
//    if (!order.getEmail().equalsIgnoreCase(email)) {
//      throw new ResourceNotFoundException("Order not found");   // deliberately same message
//    }
//    return order;
//  }
}