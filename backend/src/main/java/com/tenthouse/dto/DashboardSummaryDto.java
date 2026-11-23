package com.tenthouse.dto;
import java.math.BigDecimal;
public record DashboardSummaryDto(long totalItems, long totalCategories, long totalCustomers, long totalBookings,
    long todaysEvents, long upcomingEvents, BigDecimal monthlyIncome, BigDecimal pendingPayments, BigDecimal totalExpenses) {}