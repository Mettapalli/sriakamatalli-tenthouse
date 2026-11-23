package com.tenthouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name = "booking_items")
public class BookingItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @JsonIgnore @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "booking_id", nullable = false) private Booking booking;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "item_id", nullable = false) private Item item;
    @Column(nullable = false) private Integer quantity;
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2) private BigDecimal unitPrice;
    @Column(name = "price_unit", nullable = false, length = 30) private String priceUnit;
    @Column(name = "line_total", nullable = false, precision = 12, scale = 2) private BigDecimal lineTotal;
    @Column(name = "dispatched_qty", nullable = false) private Integer dispatchedQty = 0;
    @Column(name = "returned_qty", nullable = false) private Integer returnedQty = 0;
    @Column(name = "damaged_qty", nullable = false) private Integer damagedQty = 0;
    @Column(name = "missing_qty", nullable = false) private Integer missingQty = 0;
    @Column(length = 255) private String notes;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Booking getBooking() { return booking; } public void setBooking(Booking booking) { this.booking = booking; }
    public Item getItem() { return item; } public void setItem(Item item) { this.item = item; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; } public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public String getPriceUnit() { return priceUnit; } public void setPriceUnit(String priceUnit) { this.priceUnit = priceUnit; }
    public BigDecimal getLineTotal() { return lineTotal; } public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
    public Integer getDispatchedQty() { return dispatchedQty; } public void setDispatchedQty(Integer dispatchedQty) { this.dispatchedQty = dispatchedQty; }
    public Integer getReturnedQty() { return returnedQty; } public void setReturnedQty(Integer returnedQty) { this.returnedQty = returnedQty; }
    public Integer getDamagedQty() { return damagedQty; } public void setDamagedQty(Integer damagedQty) { this.damagedQty = damagedQty; }
    public Integer getMissingQty() { return missingQty; } public void setMissingQty(Integer missingQty) { this.missingQty = missingQty; }
    public String getNotes() { return notes; } public void setNotes(String notes) { this.notes = notes; }
}