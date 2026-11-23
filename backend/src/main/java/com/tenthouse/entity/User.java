package com.tenthouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "users")
public class User {
    public enum Role { ADMIN, STAFF }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 50) private String username;
    @Column(nullable = false, unique = true, length = 100) private String email;
    @JsonIgnore @Column(name = "password_hash", nullable = false) private String passwordHash;
    @Column(name = "full_name", nullable = false, length = 100) private String fullName;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Role role = Role.ADMIN;
    @Column(name = "is_active", nullable = false) private Boolean isActive = true;
    @Column(name = "created_at", insertable = false, updatable = false) private LocalDateTime createdAt;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; } public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; } public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getFullName() { return fullName; } public void setFullName(String fullName) { this.fullName = fullName; }
    public Role getRole() { return role; } public void setRole(Role role) { this.role = role; }
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}