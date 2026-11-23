package com.tenthouse.dto;
import jakarta.validation.constraints.NotBlank;
public class CategoryDto {
    @NotBlank private String name;
    private String nameTe;
    private String description;
    private Integer displayOrder = 0;
    private Boolean isActive = true;
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getNameTe() { return nameTe; } public void setNameTe(String nameTe) { this.nameTe = nameTe; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public Integer getDisplayOrder() { return displayOrder; } public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}