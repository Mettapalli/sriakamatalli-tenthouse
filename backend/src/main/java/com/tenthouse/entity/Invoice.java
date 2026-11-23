package com.tenthouse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Entity @Table(name = "invoices")
public class Invoice {
    public enum PaymentStatus { UNPAID, PARTIALLY_PAID, PAID }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "invoice_number", nullable = false, unique = true, length = 40) private String invoiceNumber;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "booking_id", nullable = false) private Booking booking;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "customer_id", nullable = false) private Customer customer;
    @Column(name = "invoice_date", nullable = false) private LocalDate invoiceDate;
    @Column(name = "due_date") private LocalDate dueDate;
    @Column(name = "items_subtotal", nullable = false, precision = 12, scale = 2) private BigDecimal itemsSubtotal = BigDecimal.ZERO;
    @Column(name = "additional_charges_total", nullable = false, precision = 12, scale = 2) private BigDecimal additionalChargesTotal = BigDecimal.ZERO;
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2) private BigDecimal discountAmount = BigDecimal.ZERO;
    @Column(name = "tax_percentage", nullable = false, precision = 5, scale = 2) private BigDecimal taxPercentage = BigDecimal.ZERO;
    @Column(name = "tax_amount", nullable = false, precision = 10, scale = 2) private BigDecimal taxAmount = BigDecimal.ZERO;
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2) private BigDecimal totalAmount = BigDecimal.ZERO;
    @Column(name = "paid_amount", nullable = false, precision = 12, scale = 2) private BigDecimal paidAmount = BigDecimal.ZERO;
    @Column(name = "balance_amount", nullable = false, precision = 12, scale = 2) private BigDecimal balanceAmount = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING) @Column(name = "payment_status", nullable = false) private PaymentStatus paymentStatus = PaymentStatus.UNPAID;
    @Column(columnDefinition = "TEXT") private String notes;
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) private List<InvoiceItem> items = new ArrayList<>();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getInvoiceNumber() { return invoiceNumber; } public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public Booking getBooking() { return booking; } public void setBooking(Booking booking) { this.booking = booking; }
    public Customer getCustomer() { return customer; } public void setCustomer(Customer customer) { this.customer = customer; }
    public LocalDate getInvoiceDate() { return invoiceDate; } public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }
    public LocalDate getDueDate() { return dueDate; } public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public BigDecimal getItemsSubtotal() { return itemsSubtotal; } public void setItemsSubtotal(BigDecimal itemsSubtotal) { this.itemsSubtotal = itemsSubtotal; }
    public BigDecimal getAdditionalChargesTotal() { return additionalChargesTotal; } public void setAdditionalChargesTotal(BigDecimal additionalChargesTotal) { this.additionalChargesTotal = additionalChargesTotal; }
    public BigDecimal getDiscountAmount() { return discountAmount; } public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public BigDecimal getTaxPercentage() { return taxPercentage; } public void setTaxPercentage(BigDecimal taxPercentage) { this.taxPercentage = taxPercentage; }
    public BigDecimal getTaxAmount() { return taxAmount; } public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; } public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getPaidAmount() { return paidAmount; } public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
    public BigDecimal getBalanceAmount() { return balanceAmount; } public void setBalanceAmount(BigDecimal balanceAmount) { this.balanceAmount = balanceAmount; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; } public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getNotes() { return notes; } public void setNotes(String notes) { this.notes = notes; }
    public List<InvoiceItem> getItems() { return items; } public void setItems(List<InvoiceItem> items) { this.items = items; }
}