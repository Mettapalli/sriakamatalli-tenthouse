package com.tenthouse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Entity @Table(name = "quotations")
public class Quotation {
    public enum QuotationStatus { DRAFT, SENT, ACCEPTED, REJECTED, EXPIRED }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "quotation_number", nullable = false, unique = true, length = 40) private String quotationNumber;
    @Column(name = "booking_id") private Long bookingId;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "customer_id", nullable = false) private Customer customer;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "event_id") private Event event;
    @Column(name = "quotation_date", nullable = false) private LocalDate quotationDate;
    @Column(name = "valid_until") private LocalDate validUntil;
    @Column(name = "items_subtotal", nullable = false, precision = 12, scale = 2) private BigDecimal itemsSubtotal = BigDecimal.ZERO;
    @Column(name = "additional_charges_total", nullable = false, precision = 12, scale = 2) private BigDecimal additionalChargesTotal = BigDecimal.ZERO;
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2) private BigDecimal discountAmount = BigDecimal.ZERO;
    @Column(name = "grand_total", nullable = false, precision = 12, scale = 2) private BigDecimal grandTotal = BigDecimal.ZERO;
    @Column(name = "terms_and_conditions", columnDefinition = "TEXT") private String termsAndConditions;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private QuotationStatus status = QuotationStatus.SENT;
    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) private List<QuotationItem> items = new ArrayList<>();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getQuotationNumber() { return quotationNumber; } public void setQuotationNumber(String quotationNumber) { this.quotationNumber = quotationNumber; }
    public Long getBookingId() { return bookingId; } public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public Customer getCustomer() { return customer; } public void setCustomer(Customer customer) { this.customer = customer; }
    public Event getEvent() { return event; } public void setEvent(Event event) { this.event = event; }
    public LocalDate getQuotationDate() { return quotationDate; } public void setQuotationDate(LocalDate quotationDate) { this.quotationDate = quotationDate; }
    public LocalDate getValidUntil() { return validUntil; } public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }
    public BigDecimal getItemsSubtotal() { return itemsSubtotal; } public void setItemsSubtotal(BigDecimal itemsSubtotal) { this.itemsSubtotal = itemsSubtotal; }
    public BigDecimal getAdditionalChargesTotal() { return additionalChargesTotal; } public void setAdditionalChargesTotal(BigDecimal additionalChargesTotal) { this.additionalChargesTotal = additionalChargesTotal; }
    public BigDecimal getDiscountAmount() { return discountAmount; } public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public BigDecimal getGrandTotal() { return grandTotal; } public void setGrandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; }
    public String getTermsAndConditions() { return termsAndConditions; } public void setTermsAndConditions(String termsAndConditions) { this.termsAndConditions = termsAndConditions; }
    public QuotationStatus getStatus() { return status; } public void setStatus(QuotationStatus status) { this.status = status; }
    public List<QuotationItem> getItems() { return items; } public void setItems(List<QuotationItem> items) { this.items = items; }
}