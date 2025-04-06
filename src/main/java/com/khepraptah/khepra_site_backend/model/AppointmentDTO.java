package com.khepraptah.khepra_site_backend.model;

import java.util.Date;

// Definitions: DTO means Data Transfer Object, DAO Data Access Object
public record AppointmentDTO(Long id, String type, String name, String email, double phone_number, Date date, boolean isVirtual) {
}
