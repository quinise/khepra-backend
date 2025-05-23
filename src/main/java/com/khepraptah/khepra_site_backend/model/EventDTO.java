package com.khepraptah.khepra_site_backend.model;

import java.util.Date;

public record EventDTO(
        Long id,
        String eventName,
        String eventType,
        String clientName,
        Date startDate,
        Date endDate,
        String streetAddress,
        String city,
        String state,
        Double zipCode,
        String description,
        Boolean isVirtual
) {}
