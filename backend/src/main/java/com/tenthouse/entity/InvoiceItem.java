package com.tenthouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name = "invoice_items")
public class InvoiceItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @JsonIgnore @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "invoice_id", nullable = false) private Invoice invoice;
    @Column(name = "item_id") private Long itemId;
    @Column(name = "item_name", nullable = false, length = 150) private String itemName;
    @Column(nullable = false) private Integer quantity;
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2) private BigDecimal unitPrice;
    @Column(name = "price_unit", nullable = false, length = 30) private String priceUnit;
    @Column(name = "line_total", nullable = false, precision = 12, scale = 2) private BigDecimal lineTotal;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Invoice getInvoice() { return invoice; } public void setInvoice(Invoice invoice) { this.invoice = invoice; }
    public Long getItemId() { return itemId; } public void setItemId(Long itemId) { this.itemId = itemId; }
    public String getItemName() { return itemName; } public void setItemName(String itemName) { this.itemName = itemName; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; } public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public String getPriceUnit() { return priceUnit; } public void setPriceUnit(String priceUnit) { this.priceUnit = priceUnit; }
    public BigDecimal getLineTotal() { return lineTotal; } public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
}