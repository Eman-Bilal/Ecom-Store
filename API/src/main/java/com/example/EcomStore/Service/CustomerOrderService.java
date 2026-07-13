package com.example.EcomStore.Service;

import com.example.EcomStore.Entities.CustomerOrder;
import com.example.EcomStore.Repository.CustomerOrderRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Service
@Getter
@Setter
public class CustomerOrderService {

  private CustomerOrderRepository customerOrderRepository;

  private String generateOrderNumber() {

    LocalDate today = LocalDate.now();

    LocalDateTime startOfDay = today.atStartOfDay();
    LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

    long orderCount = customerOrderRepository
        .countByPlacedAtBetween(startOfDay, endOfDay);

    String date = today.format(DateTimeFormatter.BASIC_ISO_DATE);

    return String.format("ORD-%s-%04d", date, orderCount + 1);
  }

  public CustomerOrder createOrder(CustomerOrder order) {

    order.setOrderNumber(generateOrderNumber());

    return customerOrderRepository.save(order);
  }
}
