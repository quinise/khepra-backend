package com.khepraptah.khepra_site_backend.model;

import java.time.LocalDateTime;
import java.util.Date;

public class EventDTO {
    private Long id;
    private String eventName;
    private String eventType;
    private String clientName;
    private Date startDate;
    private Date endDate;
    private int durationMinutes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String streetAddress;
    private String city;
    private String state;
    private Double zipCode;
    private String description;
    private Boolean isVirtual;

    // Constructors
    public EventDTO() {}

    public EventDTO(Long id, String eventName, String eventType, String clientName,
                    Date startDate, Date endDate, int durationMinutes,
                    LocalDateTime startTime, LocalDateTime endTime,
                    String streetAddress, String city, String state,
                    Double zipCode, String description, Boolean isVirtual) {
        this.id = id;
        this.eventName = eventName;
        this.eventType = eventType;
        this.clientName = clientName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationMinutes = durationMinutes;
        this.startTime = startTime;
        this.endTime = endTime;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.description = description;
        this.isVirtual = isVirtual;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public Double getZipCode() { return zipCode; }
    public void setZipCode(Double zipCode) { this.zipCode = zipCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsVirtual() { return isVirtual; }
    public void setIsVirtual(Boolean isVirtual) { this.isVirtual = isVirtual; }
}
