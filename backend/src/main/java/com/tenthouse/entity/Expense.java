package com.tenthouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name = "expenses")
public class Expense {
    public enum ExpenseCategory { TRANSPORTATION, LABOUR, REPAIRS, MAINTENANCE, ELECTRICITY, PURCHASES, FUEL, OTHER }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotNull @Column(name = "expense_date", nullable = false) private LocalDate expenseDate;
    @NotNull @Enumerated(EnumType.STRING) @Column(nullable = false) private ExpenseCategory category;
    @NotBlank @Column(nullable = false, length = 255) private String description;
    @NotNull @DecimalMin("0.01") @Column(nullable = false, precision = 12, scale = 2) private BigDecimal amount;
    @Enumerated(EnumType.STRING) @Column(name = "payment_method", nullable = false) private Payment.PaymentMethod paymentMethod = Payment.PaymentMethod.CASH;
    @Column(name = "booking_id") private Long bookingId;
    @Column(columnDefinition = "TEXT") private String notes;
    @Column(name = "recorded_by") private Long recordedBy;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public LocalDate getExpenseDate() { return expenseDate; } public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }
    public ExpenseCategory getCategory() { return category; } public void setCategory(ExpenseCategory category) { this.category = category; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Payment.PaymentMethod getPaymentMethod() { return paymentMethod; } public void setPaymentMethod(Payment.PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public Long getBookingId() { return bookingId; } public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public String getNotes() { return notes; } public void setNotes(String notes) { this.notes = notes; }
    public Long getRecordedBy() { return recordedBy; } public void setRecordedBy(Long recordedBy) { this.recordedBy = recordedBy; }
}