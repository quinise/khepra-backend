package com.khepraptah.khepra_site_backend.service;

import com.khepraptah.khepra_site_backend.exception.ResourceNotFoundException;
import com.khepraptah.khepra_site_backend.exception.UnauthorizedException;
import com.khepraptah.khepra_site_backend.model.Availability;
import com.khepraptah.khepra_site_backend.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    public Availability updateAvailability(Long id, Availability availability, boolean isAdmin) {
        if (!isAdmin) {
            throw new UnauthorizedException("You are not authorized to update availability.");
        }

        // Check if the availability exists
        Availability existingAvailability = availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found"));

        // Update the availability fields
        existingAvailability.setSelectedDate(availability.getSelectedDate());
        existingAvailability.setStartTime(availability.getStartTime());
        existingAvailability.setEndTime(availability.getEndTime());

        return availabilityRepository.save(existingAvailability);
    }
}