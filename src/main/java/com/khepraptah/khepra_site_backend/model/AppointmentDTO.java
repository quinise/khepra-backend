package com.khepraptah.khepra_site_backend.model;

import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record AppointmentDTO(Long id, String type, String userId, String name, String email, String phoneNumber, Date date, Boolean isVirtual) {
    public LocalDateTime getDate() {
        return date == null ? null : date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }
}


