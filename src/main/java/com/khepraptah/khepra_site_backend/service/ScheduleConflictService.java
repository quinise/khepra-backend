package com.khepraptah.khepra_site_backend.service;

import com.khepraptah.khepra_site_backend.exception.ConflictException;
import com.khepraptah.khepra_site_backend.model.Appointment;
import com.khepraptah.khepra_site_backend.model.Event;
import com.khepraptah.khepra_site_backend.model.Schedulable;
import com.khepraptah.khepra_site_backend.repository.AppointmentRepository;
import com.khepraptah.khepra_site_backend.repository.EventRepository;
import com.khepraptah.khepra_site_backend.util.BufferUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleConflictService {
    private final AppointmentRepository appointmentRepo;
    private final EventRepository eventRepo;

    public ScheduleConflictService(AppointmentRepository appointmentRepo, EventRepository eventRepo) {
        this.appointmentRepo = appointmentRepo;
        this.eventRepo = eventRepo;
    }

    /**
     * Checks if a schedulable item conflicts with existing appointments or events,
     * considering buffer times.
     */
    @Transactional
    public boolean checkForConflicts(Schedulable newItem) {
        if (newItem.getStartTime() == null || newItem.getEndTime() == null) {
            throw new IllegalArgumentException("Start time or end time cannot be null when checking for conflicts.");
        }

        LocalDateTime newStart = newItem.getStartTime();
        LocalDateTime newEnd = newItem.getEndTime();

        System.out.println("✅ Checking conflicts for desired start = " + newStart + ", end = " + newEnd);

        // Fetch all existing items (appointments + events)
        List<Schedulable> existingItems = new ArrayList<>();
        existingItems.addAll(appointmentRepo.findAll());
        existingItems.addAll(eventRepo.findAll());

        for (Schedulable existing : existingItems) {
            if (existing.equals(newItem)) continue;

            LocalDateTime existingStart = existing.getStartTime();
            LocalDateTime existingEnd = existing.getEndTime();
            Duration buffer = BufferUtils.getBufferDuration(existing);

            // BLOCKING RULE 1: Time overlap (normal)
            boolean overlaps =
                    newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
            if (overlaps) {
                System.out.println("❌ Overlaps existing item: " + existing);
                return true;
            }

            // BLOCKING RULE 2: Violates buffer window AFTER existing item
            LocalDateTime blockedUntil = existingEnd.plus(buffer);
            if (!newStart.isBefore(existingStart) && newStart.isBefore(blockedUntil)) {
                System.out.println("❌ Violates buffer window after: " + existing);
                return true;
            }
        }

        return false;
    }
}
