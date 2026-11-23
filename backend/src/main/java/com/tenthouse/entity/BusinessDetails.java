package com.tenthouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity @Table(name = "business_details")
public class BusinessDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(name = "business_name", nullable = false, length = 150) private String businessName;
    @Column(length = 200) private String tagline;
    @NotBlank @Column(name = "owner_names", nullable = false, length = 150) private String ownerNames;
    @NotBlank @Column(name = "primary_mobile", nullable = false, length = 20) private String primaryMobile;
    @Column(name = "secondary_mobile", length = 20) private String secondaryMobile;
    @Column(name = "whatsapp_primary", length = 20) private String whatsappPrimary;
    @Column(name = "whatsapp_secondary", length = 20) private String whatsappSecondary;
    @Column(length = 100) private String email;
    @Column(length = 255) private String address;
    @Column(length = 100) private String city;
    @Column(length = 100) private String state;
    @Column(name = "pin_code", length = 15) private String pinCode;
    @Column(name = "gst_number", length = 30) private String gstNumber;
    @Column(name = "website_name", length = 150) private String websiteName;
    @Column(name = "logo_path", length = 255) private String logoPath;
    @Column(name = "terms_and_conditions", columnDefinition = "TEXT") private String termsAndConditions;
    @Column(name = "updated_at", insertable = false, updatable = false) private LocalDateTime updatedAt;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getBusinessName() { return businessName; } public void setBusinessName(String businessName) { this.businessName = businessName; }
    public String getTagline() { return tagline; } public void setTagline(String tagline) { this.tagline = tagline; }
    public String getOwnerNames() { return ownerNames; } public void setOwnerNames(String ownerNames) { this.ownerNames = ownerNames; }
    public String getPrimaryMobile() { return primaryMobile; } public void setPrimaryMobile(String primaryMobile) { this.primaryMobile = primaryMobile; }
    public String getSecondaryMobile() { return secondaryMobile; } public void setSecondaryMobile(String secondaryMobile) { this.secondaryMobile = secondaryMobile; }
    public String getWhatsappPrimary() { return whatsappPrimary; } public void setWhatsappPrimary(String whatsappPrimary) { this.whatsappPrimary = whatsappPrimary; }
    public String getWhatsappSecondary() { return whatsappSecondary; } public void setWhatsappSecondary(String whatsappSecondary) { this.whatsappSecondary = whatsappSecondary; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; } public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; } public void setCity(String city) { this.city = city; }
    public String getState() { return state; } public void setState(String state) { this.state = state; }
    public String getPinCode() { return pinCode; } public void setPinCode(String pinCode) { this.pinCode = pinCode; }
    public String getGstNumber() { return gstNumber; } public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }
    public String getWebsiteName() { return websiteName; } public void setWebsiteName(String websiteName) { this.websiteName = websiteName; }
    public String getLogoPath() { return logoPath; } public void setLogoPath(String logoPath) { this.logoPath = logoPath; }
    public String getTermsAndConditions() { return termsAndConditions; } public void setTermsAndConditions(String termsAndConditions) { this.termsAndConditions = termsAndConditions; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}