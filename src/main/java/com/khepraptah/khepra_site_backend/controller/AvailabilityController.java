package com.khepraptah.khepra_site_backend.controller;

import com.khepraptah.khepra_site_backend.model.Availability;
import com.khepraptah.khepra_site_backend.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    @PutMapping("/{id}")
    public Availability updateAvailability(
            @PathVariable Long id,
            @RequestBody Availability availability,
            @RequestAttribute("isAdmin") boolean isAdmin) { // Retrieve isAdmin from the request
        return availabilityService.updateAvailability(id, availability, isAdmin);
    }
}