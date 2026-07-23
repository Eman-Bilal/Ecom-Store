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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
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
  private final EmailService emailService;

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

    BigDecimal shippingFee = BigDecimal.valueOf(200);
    BigDecimal totalAmount = subtotal.add(shippingFee);

    order.setSubtotal(subtotal);
    order.setShippingFee(shippingFee);
    order.setTotalAmount(totalAmount);

    CustomerOrder savedOrder = customerOrderRepository.save(order);
    orderItemsList.forEach(item -> item.setOrder(savedOrder));
    orderItemsRepository.saveAll(orderItemsList);

    // Notify admin
    emailService.sendAdminNotification(
        "New Order Placed: " + savedOrder.getOrderNumber(),
        "A new order was placed by " + savedOrder.getFirstName() + " " + savedOrder.getLastName()
            + " (" + savedOrder.getEmail() + ").\n"
            + "Order Number: " + savedOrder.getOrderNumber() + "\n"
            + "Status: " + savedOrder.getOrderStatus().name() + "\n"
            + "Total: Rs. " + savedOrder.getTotalAmount()
    );

    // Notify customer
    emailService.sendEmail(savedOrder.getEmail(),
        "Order Confirmation: " + savedOrder.getOrderNumber(),
        "Hi " + savedOrder.getFirstName() + ",\n\n"
            + "Thank you for your order! Here are your order details:\n"
            + "Order Number: " + savedOrder.getOrderNumber() + "\n"
            + "Status: " + savedOrder.getOrderStatus().name() + "\n"
            + "Total: Rs. " + savedOrder.getTotalAmount() + "\n\n"
            + "You can track your order anytime using your order number and email.\n\n"
            + "Thank you for shopping with us!"
    );


    return savedOrder;
  }

  public CustomerOrder getById(String id) {
    return customerOrderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
  }

  public List<CustomerOrder> getAll() {
    return customerOrderRepository.findAll();
  }

  public CustomerOrder getByOrderNumber(String orderNumber) {
    return customerOrderRepository.findByOrderNumber(orderNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with order number: " + orderNumber));
  }

  public CustomerOrder getByOrderNumberAndEmail(String orderNumber, String email) {
    CustomerOrder order = customerOrderRepository.findByOrderNumber(orderNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    if (!order.getEmail().equalsIgnoreCase(email)) {
      throw new ResourceNotFoundException("Order not found");
    }
    return order;
  }

  public CustomerOrder updateOrderStatus(String id, OrderStatus newStatus) {
    CustomerOrder order = getById(id);

    if (order.getOrderStatus() == OrderStatus.DELIVERED || order.getOrderStatus() == OrderStatus.CANCELLED) {
      throw new IllegalStateException("Cannot change status of a finalized order");
    }

    order.setOrderStatus(newStatus);
    CustomerOrder saved = customerOrderRepository.save(order);

    emailService.sendEmail(saved.getEmail(),
        "Order " + saved.getOrderNumber() + " Update",
        "Your order status has been updated to: " + newStatus.name());

    return saved;
  }

  @Transactional
  public CustomerOrder cancelOrder(CustomerOrder order) {
    if (order.getOrderStatus() == OrderStatus.SHIPPED
        || order.getOrderStatus() == OrderStatus.DELIVERED
        || order.getOrderStatus() == OrderStatus.CANCELLED) {
      throw new IllegalStateException(
          "Cannot cancel an order that is already " + order.getOrderStatus().name());
    }

    List<OrderItems> items = orderItemsRepository.findByOrder_Id(order.getId());
    for (OrderItems item : items) {
      Product product = item.getProduct();
      product.setQuantityInStock(product.getQuantityInStock() + item.getQuantity());
      productService.syncStatus(product);
      productRepository.save(product);
    }

    order.setOrderStatus(OrderStatus.CANCELLED);
    CustomerOrder saved = customerOrderRepository.save(order);

    emailService.sendEmail(saved.getEmail(),
        "Order " + saved.getOrderNumber() + " Cancelled",
        "Your order has been cancelled. If you didn't request this, please contact us.");

    emailService.sendAdminNotification(
        "Order Cancelled: " + saved.getOrderNumber(),
        "Order " + saved.getOrderNumber() + " (" + saved.getEmail() + ") was cancelled.");

    return saved;
  }

  public CustomerOrder cancelOrderById(String id) {
    return cancelOrder(getById(id));
  }

  public CustomerOrder cancelOrderByCustomer(String orderNumber, String email) {
    return cancelOrder(getByOrderNumberAndEmail(orderNumber, email));
  }
}