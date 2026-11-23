package com.tenthouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "item_price_history")
public class ItemPriceHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @JsonIgnore @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "item_id", nullable = false) private Item item;
    @Column(name = "old_price", nullable = false, precision = 10, scale = 2) private BigDecimal oldPrice;
    @Column(name = "new_price", nullable = false, precision = 10, scale = 2) private BigDecimal newPrice;
    @Column(name = "old_unit", nullable = false, length = 30) private String oldUnit;
    @Column(name = "new_unit", nullable = false, length = 30) private String newUnit;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "changed_by") private User changedBy;
    @Column(name = "change_reason", length = 255) private String changeReason;
    @Column(name = "changed_at", insertable = false, updatable = false) private LocalDateTime changedAt;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Item getItem() { return item; } public void setItem(Item item) { this.item = item; }
    public BigDecimal getOldPrice() { return oldPrice; } public void setOldPrice(BigDecimal oldPrice) { this.oldPrice = oldPrice; }
    public BigDecimal getNewPrice() { return newPrice; } public void setNewPrice(BigDecimal newPrice) { this.newPrice = newPrice; }
    public String getOldUnit() { return oldUnit; } public void setOldUnit(String oldUnit) { this.oldUnit = oldUnit; }
    public String getNewUnit() { return newUnit; } public void setNewUnit(String newUnit) { this.newUnit = newUnit; }
    public User getChangedBy() { return changedBy; } public void setChangedBy(User changedBy) { this.changedBy = changedBy; }
    public String getChangeReason() { return changeReason; } public void setChangeReason(String changeReason) { this.changeReason = changeReason; }
    public LocalDateTime getChangedAt() { return changedAt; }
}