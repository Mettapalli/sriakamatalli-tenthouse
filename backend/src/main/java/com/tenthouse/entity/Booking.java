package com.tenthouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Entity @Table(name = "bookings")
public class Booking {
    public enum BookingStatus { ENQUIRY, QUOTATION_SENT, PENDING_CONFIRMATION, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED }
    public enum DiscountType { NONE, FIXED, PERCENTAGE }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "booking_number", nullable = false, unique = true, length = 40) private String bookingNumber;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "customer_id", nullable = false) private Customer customer;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "event_id", nullable = false) private Event event;
    @Column(name = "start_date", nullable = false) private LocalDate startDate;
    @Column(name = "end_date", nullable = false) private LocalDate endDate;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private BookingStatus status = BookingStatus.CONFIRMED;
    @Column(name = "items_subtotal", nullable = false, precision = 12, scale = 2) private BigDecimal itemsSubtotal = BigDecimal.ZERO;
    @Column(name = "transportation_charge", nullable = false, precision = 10, scale = 2) private BigDecimal transportationCharge = BigDecimal.ZERO;
    @Column(name = "labour_charge", nullable = false, precision = 10, scale = 2) private BigDecimal labourCharge = BigDecimal.ZERO;
    @Column(name = "decoration_charge", nullable = false, precision = 10, scale = 2) private BigDecimal decorationCharge = BigDecimal.ZERO;
    @Column(name = "installation_charge", nullable = false, precision = 10, scale = 2) private BigDecimal installationCharge = BigDecimal.ZERO;
    @Column(name = "generator_charge", nullable = false, precision = 10, scale = 2) private BigDecimal generatorCharge = BigDecimal.ZERO;
    @Column(name = "other_charges", nullable = false, precision = 10, scale = 2) private BigDecimal otherCharges = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING) @Column(name = "discount_type", nullable = false) private DiscountType discountType = DiscountType.NONE;
    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2) private BigDecimal discountValue = BigDecimal.ZERO;
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2) private BigDecimal discountAmount = BigDecimal.ZERO;
    @Column(name = "final_amount", nullable = false, precision = 12, scale = 2) private BigDecimal finalAmount = BigDecimal.ZERO;
    @Column(name = "advance_paid", nullable = false, precision = 12, scale = 2) private BigDecimal advancePaid = BigDecimal.ZERO;
    @Column(name = "balance_amount", nullable = false, precision = 12, scale = 2) private BigDecimal balanceAmount = BigDecimal.ZERO;
    @Column(columnDefinition = "TEXT") private String notes;
    @JsonIgnore @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "created_by") private User createdBy;
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) private List<BookingItem> items = new ArrayList<>();
    @Column(name = "created_at", insertable = false, updatable = false) private LocalDateTime createdAt;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getBookingNumber() { return bookingNumber; } public void setBookingNumber(String bookingNumber) { this.bookingNumber = bookingNumber; }
    public Customer getCustomer() { return customer; } public void setCustomer(Customer customer) { this.customer = customer; }
    public Event getEvent() { return event; } public void setEvent(Event event) { this.event = event; }
    public LocalDate getStartDate() { return startDate; } public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; } public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public BookingStatus getStatus() { return status; } public void setStatus(BookingStatus status) { this.status = status; }
    public BigDecimal getItemsSubtotal() { return itemsSubtotal; } public void setItemsSubtotal(BigDecimal itemsSubtotal) { this.itemsSubtotal = itemsSubtotal; }
    public BigDecimal getTransportationCharge() { return transportationCharge; } public void setTransportationCharge(BigDecimal transportationCharge) { this.transportationCharge = transportationCharge; }
    public BigDecimal getLabourCharge() { return labourCharge; } public void setLabourCharge(BigDecimal labourCharge) { this.labourCharge = labourCharge; }
    public BigDecimal getDecorationCharge() { return decorationCharge; } public void setDecorationCharge(BigDecimal decorationCharge) { this.decorationCharge = decorationCharge; }
    public BigDecimal getInstallationCharge() { return installationCharge; } public void setInstallationCharge(BigDecimal installationCharge) { this.installationCharge = installationCharge; }
    public BigDecimal getGeneratorCharge() { return generatorCharge; } public void setGeneratorCharge(BigDecimal generatorCharge) { this.generatorCharge = generatorCharge; }
    public BigDecimal getOtherCharges() { return otherCharges; } public void setOtherCharges(BigDecimal otherCharges) { this.otherCharges = otherCharges; }
    public DiscountType getDiscountType() { return discountType; } public void setDiscountType(DiscountType discountType) { this.discountType = discountType; }
    public BigDecimal getDiscountValue() { return discountValue; } public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    public BigDecimal getDiscountAmount() { return discountAmount; } public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public BigDecimal getFinalAmount() { return finalAmount; } public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }
    public BigDecimal getAdvancePaid() { return advancePaid; } public void setAdvancePaid(BigDecimal advancePaid) { this.advancePaid = advancePaid; }
    public BigDecimal getBalanceAmount() { return balanceAmount; } public void setBalanceAmount(BigDecimal balanceAmount) { this.balanceAmount = balanceAmount; }
    public String getNotes() { return notes; } public void setNotes(String notes) { this.notes = notes; }
    public User getCreatedBy() { return createdBy; } public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    public List<BookingItem> getItems() { return items; } public void setItems(List<BookingItem> items) { this.items = items; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}