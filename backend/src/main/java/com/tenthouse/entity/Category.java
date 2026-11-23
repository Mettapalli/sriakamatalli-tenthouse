package com.tenthouse.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "categories")
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 100) private String name;
    @Column(name = "name_te", length = 150) private String nameTe;
    @Column(length = 255) private String description;
    @Column(name = "display_order", nullable = false) private Integer displayOrder = 0;
    @Column(name = "is_active", nullable = false) private Boolean isActive = true;
    @Column(name = "created_at", insertable = false, updatable = false) private LocalDateTime createdAt;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getNameTe() { return nameTe; } public void setNameTe(String nameTe) { this.nameTe = nameTe; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public Integer getDisplayOrder() { return displayOrder; } public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}