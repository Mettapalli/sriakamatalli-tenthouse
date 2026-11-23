package com.tenthouse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name = "payments")
public class Payment {
    public enum PaymentMethod { CASH, UPI, BANK_TRANSFER, OTHER }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "receipt_number", nullable = false, unique = true, length = 40) private String receiptNumber;
    @Column(name = "booking_id", nullable = false) private Long bookingId;
    @Column(name = "invoice_id") private Long invoiceId;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "customer_id", nullable = false) private Customer customer;
    @Column(name = "payment_date", nullable = false) private LocalDate paymentDate;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal amount;
    @Enumerated(EnumType.STRING) @Column(name = "payment_method", nullable = false) private PaymentMethod paymentMethod = PaymentMethod.CASH;
    @Column(name = "reference_number", length = 100) private String referenceNumber;
    @Column(name = "is_advance", nullable = false) private Boolean isAdvance = false;
    @Column(length = 255) private String notes;
    @Column(name = "received_by") private Long receivedBy;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getReceiptNumber() { return receiptNumber; } public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }
    public Long getBookingId() { return bookingId; } public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public Long getInvoiceId() { return invoiceId; } public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }
    public Customer getCustomer() { return customer; } public void setCustomer(Customer customer) { this.customer = customer; }
    public LocalDate getPaymentDate() { return paymentDate; } public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal amount) { this.amount = amount; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; } public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getReferenceNumber() { return referenceNumber; } public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    public Boolean getIsAdvance() { return isAdvance; } public void setIsAdvance(Boolean isAdvance) { this.isAdvance = isAdvance; }
    public String getNotes() { return notes; } public void setNotes(String notes) { this.notes = notes; }
    public Long getReceivedBy() { return receivedBy; } public void setReceivedBy(Long receivedBy) { this.receivedBy = receivedBy; }
}