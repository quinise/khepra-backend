package com.khepraptah.khepra_site_backend.service;

import com.khepraptah.khepra_site_backend.model.Appointment;
import com.khepraptah.khepra_site_backend.model.Schedulable;

import com.khepraptah.khepra_site_backend.repository.AppointmentRepository;
import com.khepraptah.khepra_site_backend.repository.EventRepository;
import com.khepraptah.khepra_site_backend.util.BufferUtils;
import com.khepraptah.khepra_site_backend.exception.ConflictException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScheduleConflictServiceTest {

    private AppointmentRepository appointmentRepo;
    private EventRepository eventRepo;
    private ScheduleConflictService conflictService;

    @BeforeEach
    public void setUp() {
        appointmentRepo = mock(AppointmentRepository.class);
        eventRepo = mock(EventRepository.class);
        conflictService = new ScheduleConflictService(appointmentRepo, eventRepo);
    }

    @Test
    public void testNoConflict_shouldPass() {
        // Existing appointment from 10:00 to 11:00
        Appointment existing = new Appointment();
        existing.setStartTime(LocalDateTime.of(2025, 6, 1, 10, 0));
        existing.setEndTime(LocalDateTime.of(2025, 6, 1, 11, 0));
        existing.setCity("Bremerton"); // 2.5 hour buffer

        when(appointmentRepo.findAll()).thenReturn(List.of(existing));
        when(eventRepo.findAll()).thenReturn(Collections.emptyList());

        // New appointment starts well after buffer: 11:00 + 2.5 hours = 13:30
        Appointment newAppointment = new Appointment();
        newAppointment.setStartTime(LocalDateTime.of(2025, 6, 1, 14, 0));
        newAppointment.setEndTime(LocalDateTime.of(2025, 6, 1, 15, 0));
        newAppointment.setCity("Bremerton");

        assertDoesNotThrow(() -> conflictService.checkForConflicts(newAppointment));
    }

    @Test
    public void testOverlapConflict_shouldThrow() {
        // Existing appointment from 10:00 to 11:00
        Appointment existing = new Appointment();
    }
}
