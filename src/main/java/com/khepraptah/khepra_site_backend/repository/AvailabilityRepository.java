package com.khepraptah.khepra_site_backend.repository;

import com.khepraptah.khepra_site_backend.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
}