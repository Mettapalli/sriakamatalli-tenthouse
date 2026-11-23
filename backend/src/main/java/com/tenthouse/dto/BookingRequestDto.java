package com.tenthouse.dto;
import com.tenthouse.entity.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
public class BookingRequestDto {
    @NotNull private Long customerId;
    @NotBlank private String eventName;
    @NotNull private Event.EventType eventType = Event.EventType.WEDDING;
    @NotNull private LocalDate startDate;
    @NotNull private LocalDate endDate;
    @NotBlank private String eventAddress;
    private Integer guestCount;
    private String specialInstructions;
    private Booking.BookingStatus status = Booking.BookingStatus.CONFIRMED;
    private BigDecimal transportationCharge = BigDecimal.ZERO;
    private BigDecimal labourCharge = BigDecimal.ZERO;
    private BigDecimal decorationCharge = BigDecimal.ZERO;
    private BigDecimal installationCharge = BigDecimal.ZERO;
    private BigDecimal generatorCharge = BigDecimal.ZERO;
    private BigDecimal otherCharges = BigDecimal.ZERO;
    private Booking.DiscountType discountType = Booking.DiscountType.NONE;
    private BigDecimal discountValue = BigDecimal.ZERO;
    private BigDecimal advancePaid = BigDecimal.ZERO;
    private String notes;
    @NotEmpty private List<ItemLine> items;

    public static class ItemLine {
        @NotNull private Long itemId;
        @NotNull private Integer quantity;
        private BigDecimal unitPrice;
        public Long getItemId() { return itemId; } public void setItemId(Long itemId) { this.itemId = itemId; }
        public Integer getQuantity() { return quantity; } public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; } public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    }

    public Long getCustomerId() { return customerId; } public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getEventName() { return eventName; } public void setEventName(String eventName) { this.eventName = eventName; }
    public Event.EventType getEventType() { return eventType; } public void setEventType(Event.EventType eventType) { this.eventType = eventType; }
    public LocalDate getStartDate() { return startDate; } public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; } public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getEventAddress() { return eventAddress; } public void setEventAddress(String eventAddress) { this.eventAddress = eventAddress; }
    public Integer getGuestCount() { return guestCount; } public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }
    public String getSpecialInstructions() { return specialInstructions; } public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    public Booking.BookingStatus getStatus() { return status; } public void setStatus(Booking.BookingStatus status) { this.status = status; }
    public BigDecimal getTransportationCharge() { return transportationCharge; } public void setTransportationCharge(BigDecimal transportationCharge) { this.transportationCharge = transportationCharge; }
    public BigDecimal getLabourCharge() { return labourCharge; } public void setLabourCharge(BigDecimal labourCharge) { this.labourCharge = labourCharge; }
    public BigDecimal getDecorationCharge() { return decorationCharge; } public void setDecorationCharge(BigDecimal decorationCharge) { this.decorationCharge = decorationCharge; }
    public BigDecimal getInstallationCharge() { return installationCharge; } public void setInstallationCharge(BigDecimal installationCharge) { this.installationCharge = installationCharge; }
    public BigDecimal getGeneratorCharge() { return generatorCharge; } public void setGeneratorCharge(BigDecimal generatorCharge) { this.generatorCharge = generatorCharge; }
    public BigDecimal getOtherCharges() { return otherCharges; } public void setOtherCharges(BigDecimal otherCharges) { this.otherCharges = otherCharges; }
    public Booking.DiscountType getDiscountType() { return discountType; } public void setDiscountType(Booking.DiscountType discountType) { this.discountType = discountType; }
    public BigDecimal getDiscountValue() { return discountValue; } public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    public BigDecimal getAdvancePaid() { return advancePaid; } public void setAdvancePaid(BigDecimal advancePaid) { this.advancePaid = advancePaid; }
    public String getNotes() { return notes; } public void setNotes(String notes) { this.notes = notes; }
    public List<ItemLine> getItems() { return items; } public void setItems(List<ItemLine> items) { this.items = items; }
}