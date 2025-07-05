package com.khepraptah.khepra_site_backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate selectedDate;  // The date of availability
    private LocalTime startTime;     // The start time of the availability
    private LocalTime endTime;       // The end time of the availability

    // Constructor
    public Availability(LocalDate selectedDate, LocalTime startTime, LocalTime endTime) {
        this.selectedDate = selectedDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

   // For debugging/logging.
   @Override
    public String toString() {
        return "Availability{" +
                "selectedDate=" + selectedDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}