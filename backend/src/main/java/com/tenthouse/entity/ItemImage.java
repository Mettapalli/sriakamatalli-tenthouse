package com.tenthouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "item_images")
public class ItemImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @JsonIgnore @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "item_id", nullable = false) private Item item;
    @Column(name = "image_url", nullable = false, length = 255) private String imageUrl;
    @Column(name = "is_primary", nullable = false) private Boolean isPrimary = false;
    @Column(name = "uploaded_at", insertable = false, updatable = false) private LocalDateTime uploadedAt;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Item getItem() { return item; } public void setItem(Item item) { this.item = item; }
    public String getImageUrl() { return imageUrl; } public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Boolean getIsPrimary() { return isPrimary; } public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
}