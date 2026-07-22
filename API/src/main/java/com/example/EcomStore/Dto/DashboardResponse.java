package com.example.EcomStore.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardResponse {
  private BigDecimal revenueThisMonth;
  private long ordersThisWeek;
  private long lowStockCount;
  private long totalProducts;
  private long totalCategories;
  private List<TopProductDto> topProducts;
  private List<RecentOrderDto> recentOrders;
}