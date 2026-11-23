package com.tenthouse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "items")
public class Item {
    public enum PriceUnit { PER_PIECE, PER_DAY, PER_EVENT, PER_SET, PER_SQFT, PER_METRE, CUSTOM }
    public enum ItemStatus { AVAILABLE, OUT_OF_STOCK, MAINTENANCE, INACTIVE }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "item_code", nullable = false, unique = true, length = 30) private String itemCode;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "category_id", nullable = false) private Category category;
    @Column(nullable = false, length = 150) private String name;
    @Column(name = "name_te", length = 200) private String nameTe;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(name = "total_quantity", nullable = false) private Integer totalQuantity = 0;
    @Column(name = "available_quantity", nullable = false) private Integer availableQuantity = 0;
    @Column(name = "damaged_quantity", nullable = false) private Integer damagedQuantity = 0;
    @Column(name = "rental_price", nullable = false, precision = 10, scale = 2) private BigDecimal rentalPrice = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING) @Column(name = "price_unit", nullable = false) private PriceUnit priceUnit = PriceUnit.PER_DAY;
    @Column(name = "custom_unit_label", length = 50) private String customUnitLabel;
    @Column(name = "purchase_price", precision = 10, scale = 2) private BigDecimal purchasePrice;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private ItemStatus status = ItemStatus.AVAILABLE;
    @Column(columnDefinition = "TEXT") private String notes;
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.EAGER) private List<ItemImage> images = new ArrayList<>();
    @Column(name = "created_at", insertable = false, updatable = false) private LocalDateTime createdAt;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getItemCode() { return itemCode; } public void setItemCode(String itemCode) { this.itemCode = itemCode; }
    public Category getCategory() { return category; } public void setCategory(Category category) { this.category = category; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getNameTe() { return nameTe; } public void setNameTe(String nameTe) { this.nameTe = nameTe; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public Integer getTotalQuantity() { return totalQuantity; } public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    public Integer getAvailableQuantity() { return availableQuantity; } public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }
    public Integer getDamagedQuantity() { return damagedQuantity; } public void setDamagedQuantity(Integer damagedQuantity) { this.damagedQuantity = damagedQuantity; }
    public BigDecimal getRentalPrice() { return rentalPrice; } public void setRentalPrice(BigDecimal rentalPrice) { this.rentalPrice = rentalPrice; }
    public PriceUnit getPriceUnit() { return priceUnit; } public void setPriceUnit(PriceUnit priceUnit) { this.priceUnit = priceUnit; }
    public String getCustomUnitLabel() { return customUnitLabel; } public void setCustomUnitLabel(String customUnitLabel) { this.customUnitLabel = customUnitLabel; }
    public BigDecimal getPurchasePrice() { return purchasePrice; } public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
    public ItemStatus getStatus() { return status; } public void setStatus(ItemStatus status) { this.status = status; }
    public String getNotes() { return notes; } public void setNotes(String notes) { this.notes = notes; }
    public List<ItemImage> getImages() { return images; } public void setImages(List<ItemImage> images) { this.images = images; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}