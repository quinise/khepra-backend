package com.khepraptah.khepra_site_backend.model;

import java.util.Date;

public record AppointmentDTO(Long id, String type, String name, String email, double phone_number, Date date, Boolean isVirtual) {
}
