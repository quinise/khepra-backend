package com.khepraptah.khepra_site_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "events")
public class Event implements Schedulable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration")
    private int durationMinutes;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip_code")
    private Double zipCode;

    @Column(name = "description")
    private String description;

    @Column(name = "is_Virtual")
    private Boolean isVirtual;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    @Override
    public String getType() {
        return this.eventType;
    }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    @Override
    public LocalDateTime getStartTime() { return this.startTime; }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @PrePersist
    @PreUpdate
    public void calculateEndTime() {
        if (startTime != null && durationMinutes > 0) {
            this.endTime = this.startTime.plusMinutes(this.durationMinutes);
        }
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


    public int getDuration() { return durationMinutes; }
    public void setDuration(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) {this.streetAddress = streetAddress;}

    @Override
    public String getCity() { return this.city; }
    public void setCity(String city) {this.city = city;}

    public String getState() { return state; }
    public void setState(String state) {this.state = state;}

    public Double getZipCode() { return zipCode; }
    public void setZipCode(Double zipCode) { this.zipCode = zipCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) {this.description = description;}

    public Boolean getIsVirtualRaw() { return isVirtual; }

    public void setIsVirtual(Boolean isVirtual) { this.isVirtual = isVirtual; }

    @Override
    public boolean isVirtual() {
        return Boolean.TRUE.equals(isVirtual);
    }
}
