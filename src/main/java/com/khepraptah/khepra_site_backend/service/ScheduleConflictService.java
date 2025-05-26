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
    public void checkForConflicts(Schedulable newItem) {
        if (newItem.getStartTime() == null || newItem.getEndTime() == null) {
            throw new IllegalArgumentException("Start time or end time cannot be null when checking for conflicts.");
        }

        Duration buffer = BufferUtils.getBufferDuration(newItem);
        LocalDateTime bufferedStart = newItem.getStartTime().minus(buffer);
        LocalDateTime bufferedEnd = newItem.getEndTime().plus(buffer);

        List<Appointment> appointmentConflicts = appointmentRepo.findConflicts(bufferedStart, bufferedEnd)
                .stream()
                .filter(existing -> !existing.equals(newItem)) // avoid self-conflict
                .collect(Collectors.toList());

        List<Event> eventConflicts = eventRepo.findConflicts(bufferedStart, bufferedEnd)
                .stream()
                .filter(existing -> !existing.equals(newItem)) // avoid self-conflict
                .collect(Collectors.toList());

        if (!appointmentConflicts.isEmpty() || !eventConflicts.isEmpty()) {
            throw new ConflictException("‼️ Scheduling conflict detected, item not booked.");
        }
    }
}
