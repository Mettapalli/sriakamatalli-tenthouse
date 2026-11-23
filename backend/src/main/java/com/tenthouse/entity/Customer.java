package com.tenthouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity @Table(name = "customers")
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "customer_code", nullable = false, unique = true, length = 30) private String customerCode;
    @NotBlank @Column(name = "full_name", nullable = false, length = 120) private String fullName;
    @NotBlank @Column(name = "mobile_number", nullable = false, length = 20) private String mobileNumber;
    @Column(name = "whatsapp_number", length = 20) private String whatsappNumber;
    @Column(length = 100) private String email;
    @Column(length = 255) private String address;
    @Column(name = "default_event_location", length = 255) private String defaultEventLocation;
    @Column(columnDefinition = "TEXT") private String notes;
    @Column(name = "created_at", insertable = false, updatable = false) private LocalDateTime createdAt;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getCustomerCode() { return customerCode; } public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public String getFullName() { return fullName; } public void setFullName(String fullName) { this.fullName = fullName; }
    public String getMobileNumber() { return mobileNumber; } public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public String getWhatsappNumber() { return whatsappNumber; } public void setWhatsappNumber(String whatsappNumber) { this.whatsappNumber = whatsappNumber; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; } public void setAddress(String address) { this.address = address; }
    public String getDefaultEventLocation() { return defaultEventLocation; } public void setDefaultEventLocation(String defaultEventLocation) { this.defaultEventLocation = defaultEventLocation; }
    public String getNotes() { return notes; } public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}