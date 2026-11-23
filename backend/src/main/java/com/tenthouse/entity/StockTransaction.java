package com.tenthouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "stock_transactions")
public class StockTransaction {
    public enum TransactionType { PURCHASED, BOOKED, DISPATCHED, RETURNED, DAMAGED, REPAIRED, MISSING, ADJUSTMENT }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @JsonIgnore @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "item_id", nullable = false) private Item item;
    @Column(name = "booking_id") private Long bookingId;
    @Enumerated(EnumType.STRING) @Column(name = "transaction_type", nullable = false) private TransactionType transactionType;
    @Column(nullable = false) private Integer quantity;
    @Column(name = "transaction_date", insertable = false, updatable = false) private LocalDateTime transactionDate;
    @Column(length = 255) private String remarks;
    @JsonIgnore @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "created_by") private User createdBy;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Item getItem() { return item; } public void setItem(Item item) { this.item = item; }
    public Long getBookingId() { return bookingId; } public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public TransactionType getTransactionType() { return transactionType; } public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public String getRemarks() { return remarks; } public void setRemarks(String remarks) { this.remarks = remarks; }
    public User getCreatedBy() { return createdBy; } public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
}