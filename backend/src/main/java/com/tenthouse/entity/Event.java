package com.tenthouse.entity;

import jakarta.persistence.*;
import java.time.*;

@Entity @Table(name = "events")
public class Event {
    public enum EventType { WEDDING, RECEPTION, ENGAGEMENT, BIRTHDAY, ANNIVERSARY, RELIGIOUS, POLITICAL, COLLEGE, CORPORATE, OTHER }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "customer_id", nullable = false) private Customer customer;
    @Column(name = "event_name", nullable = false, length = 150) private String eventName;
    @Enumerated(EnumType.STRING) @Column(name = "event_type", nullable = false) private EventType eventType = EventType.WEDDING;
    @Column(name = "event_date", nullable = false) private LocalDate eventDate;
    @Column(name = "end_date", nullable = false) private LocalDate endDate;
    @Column(name = "start_time") private LocalTime startTime;
    @Column(name = "end_time") private LocalTime endTime;
    @Column(name = "event_address", nullable = false, length = 255) private String eventAddress;
    @Column(name = "guest_count") private Integer guestCount;
    @Column(name = "special_instructions", columnDefinition = "TEXT") private String specialInstructions;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Customer getCustomer() { return customer; } public void setCustomer(Customer customer) { this.customer = customer; }
    public String getEventName() { return eventName; } public void setEventName(String eventName) { this.eventName = eventName; }
    public EventType getEventType() { return eventType; } public void setEventType(EventType eventType) { this.eventType = eventType; }
    public LocalDate getEventDate() { return eventDate; } public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public LocalDate getEndDate() { return endDate; } public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public LocalTime getStartTime() { return startTime; } public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; } public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public String getEventAddress() { return eventAddress; } public void setEventAddress(String eventAddress) { this.eventAddress = eventAddress; }
    public Integer getGuestCount() { return guestCount; } public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }
    public String getSpecialInstructions() { return specialInstructions; } public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
}