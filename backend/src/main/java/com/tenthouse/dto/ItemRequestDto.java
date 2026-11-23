package com.tenthouse.dto;
import com.tenthouse.entity.Item;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public class ItemRequestDto {
    private String itemCode;
    @NotNull private Long categoryId;
    @NotBlank private String name;
    private String nameTe;
    private String description;
    @NotNull @Min(0) private Integer totalQuantity = 0;
    @Min(0) private Integer availableQuantity;
    @NotNull @Min(0) private Integer damagedQuantity = 0;
    @NotNull @DecimalMin("0.00") private BigDecimal rentalPrice = BigDecimal.ZERO;
    @NotNull private Item.PriceUnit priceUnit = Item.PriceUnit.PER_DAY;
    private String customUnitLabel;
    @DecimalMin("0.00") private BigDecimal purchasePrice;
    private Item.ItemStatus status = Item.ItemStatus.AVAILABLE;
    private String notes;
    private String priceChangeReason;

    public String getItemCode() { return itemCode; } public void setItemCode(String itemCode) { this.itemCode = itemCode; }
    public Long getCategoryId() { return categoryId; } public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getNameTe() { return nameTe; } public void setNameTe(String nameTe) { this.nameTe = nameTe; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public Integer getTotalQuantity() { return totalQuantity; } public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
    public Integer getAvailableQuantity() { return availableQuantity; } public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }
    public Integer getDamagedQuantity() { return damagedQuantity; } public void setDamagedQuantity(Integer damagedQuantity) { this.damagedQuantity = damagedQuantity; }
    public BigDecimal getRentalPrice() { return rentalPrice; } public void setRentalPrice(BigDecimal rentalPrice) { this.rentalPrice = rentalPrice; }
    public Item.PriceUnit getPriceUnit() { return priceUnit; } public void setPriceUnit(Item.PriceUnit priceUnit) { this.priceUnit = priceUnit; }
    public String getCustomUnitLabel() { return customUnitLabel; } public void setCustomUnitLabel(String customUnitLabel) { this.customUnitLabel = customUnitLabel; }
    public BigDecimal getPurchasePrice() { return purchasePrice; } public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
    public Item.ItemStatus getStatus() { return status; } public void setStatus(Item.ItemStatus status) { this.status = status; }
    public String getNotes() { return notes; } public void setNotes(String notes) { this.notes = notes; }
    public String getPriceChangeReason() { return priceChangeReason; } public void setPriceChangeReason(String priceChangeReason) { this.priceChangeReason = priceChangeReason; }
}