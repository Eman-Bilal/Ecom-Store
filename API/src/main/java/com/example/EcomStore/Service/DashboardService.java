package com.example.EcomStore.Service;

import com.example.EcomStore.Dto.*;
import com.example.EcomStore.Entities.CustomerOrder;
import com.example.EcomStore.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DashboardService {

  private final CustomerOrderRepository customerOrderRepository;
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final OrderItemsRepository orderItemsRepository;

  private static final int LOW_STOCK_THRESHOLD = 5;

  public DashboardResponse getDashboard() {
    LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startOfWeek = LocalDate.now().minusDays(7).atStartOfDay();

    var revenueThisMonth = customerOrderRepository.sumRevenueBetween(startOfMonth, now);
    var ordersThisWeek = customerOrderRepository.countByCreatedAtBetween(startOfWeek, now);
    var lowStockCount = productRepository.countByActiveTrueAndQuantityInStockLessThan(LOW_STOCK_THRESHOLD);
    var totalProducts = productRepository.countByActiveTrue();
    var totalCategories = categoryRepository.countByActiveTrue();

    List<TopProductDto> topProducts = orderItemsRepository
        .findTopSellingProducts(startOfMonth, now).stream()
        .limit(4)
        .map(row -> new TopProductDto((String) row[0], (Long) row[1]))
        .toList();

    List<RecentOrderDto> recentOrders = customerOrderRepository
        .findTop10ByOrderByCreatedAtDesc().stream()
        .map(this::toRecentOrderDto)
        .toList();

    return new DashboardResponse(
        revenueThisMonth, ordersThisWeek, lowStockCount, totalProducts, totalCategories,
        topProducts, recentOrders
    );
  }

  private RecentOrderDto toRecentOrderDto(CustomerOrder order) {
    return new RecentOrderDto(
        order.getOrderNumber(),
        order.getFirstName() + " " + order.getLastName(),
        order.getTotalAmount(),
        order.getOrderStatus()
    );
  }
}