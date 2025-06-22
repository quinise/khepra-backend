package com.khepraptah.khepra_site_backend.service;

import com.khepraptah.khepra_site_backend.model.Appointment;
import com.khepraptah.khepra_site_backend.model.AppointmentDTO;
import com.khepraptah.khepra_site_backend.model.AppointmentType;
import com.khepraptah.khepra_site_backend.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {

    private AppointmentRepository appointmentRepository;
    private ScheduleConflictService scheduleConflictService;
    private AppointmentServiceImpl appointmentService;

    @BeforeEach
    void setUp() {
        appointmentRepository = mock(AppointmentRepository.class);
        scheduleConflictService = mock(ScheduleConflictService.class);
        appointmentService = new AppointmentServiceImpl(appointmentRepository, scheduleConflictService);
    }

    @Test
    void testSaveAppointment() {
        LocalDateTime now = LocalDateTime.now();
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        AppointmentDTO dto = new AppointmentDTO(
                null, // ID is null for new appointment
                "READING",
                "user123",
                "Test User",
                "test@example.com",
                "555-1234",
                date,
                now,
                now.plusMinutes(30),
                30,
                "123 Street",
                "City",
                "State",
                12345.0,
                true,
                false
        );

        Appointment savedEntity = new Appointment();
        savedEntity.setId(1L);
        savedEntity.setType("READING");
        savedEntity.setUserId("user123");
        savedEntity.setName("Test User");
        savedEntity.setEmail("test@example.com");
        savedEntity.setPhoneNumber("555-1234");
        savedEntity.setDate(date);
        savedEntity.setStartTime(now);
        savedEntity.setEndTime(now.plusMinutes(30));
        savedEntity.setDuration(30);
        savedEntity.setStreetAddress("123 Street");
        savedEntity.setCity("City");
        savedEntity.setState("State");
        savedEntity.setZipCode(12345.0);
        savedEntity.setIsVirtual(true);
        savedEntity.setCreatedByAdmin(false);

        when(appointmentRepository.save(any())).thenReturn(savedEntity);

        AppointmentDTO result = appointmentService.saveAppointment(dto);

        verify(scheduleConflictService).checkForConflicts(any());
        verify(appointmentRepository).save(any());

        assertNotNull(result);
        assertEquals("Test User", result.name());
        assertEquals("READING", result.type());
        assertEquals("user123", result.userId());
    }

    @Test
    void testGetAppointmentByIdReturnsDto() {
        long id = 1L;
        LocalDateTime now = LocalDateTime.now();
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        Appointment entity = new Appointment();
        entity.setId(id);
        entity.setType("CLEANSING");
        entity.setUserId("user999");
        entity.setName("Another User");
        entity.setEmail("another@example.com");
        entity.setPhoneNumber("555-0000");
        entity.setDate(date);
        entity.setStartTime(now);
        entity.setEndTime(now.plusMinutes(45));
        entity.setDuration(45);
        entity.setStreetAddress("456 Road");
        entity.setCity("Town");
        entity.setState("Province");
        entity.setZipCode(67890.0);
        entity.setIsVirtual(false);
        entity.setCreatedByAdmin(true);

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<AppointmentDTO> result = appointmentService.getAppointmentById(id);

        assertTrue(result.isPresent());
        assertEquals("CLEANSING", result.get().type());
        assertEquals("Another User", result.get().name());
        assertEquals("user999", result.get().userId());
    }

    @Test
    void testGetAllAppointmentsByUserIdIncludingAdmin() {
        String userId = "user123";
        String email = "test@example.com";
        boolean includeAdminAppointments = true;

        Appointment userAppointment = new Appointment();
        userAppointment.setUserId(userId);
        userAppointment.setCreatedByAdmin(false);

        Appointment adminAppointment = new Appointment();
        adminAppointment.setUserId(userId);
        adminAppointment.setCreatedByAdmin(true);

        when(appointmentRepository.findByUserId(userId)).thenReturn(new ArrayList<>(Arrays.asList(adminAppointment)));
        when(appointmentRepository.findAdminCreatedAppointmentsByUserId(userId)).thenReturn(new ArrayList<>(Arrays.asList(adminAppointment)));

        List<AppointmentDTO> result = appointmentService.getAllAppointments(userId, email, includeAdminAppointments);

        assertThat(result).hasSize(2);
    }

    @Test
    void testGetAllAppointmentsByEmailIncludingAdmin() {
        String userId = null;
        String email = "test@example.com";
        boolean includeAdminAppointments = true;

        Appointment adminAppointment = new Appointment();
        adminAppointment.setEmail(email);
        adminAppointment.setCreatedByAdmin(true);

        when(appointmentRepository.findAdminCreatedAppointmentsByEmail(email)).thenReturn(new ArrayList<>(Arrays.asList(adminAppointment)));

        List<AppointmentDTO> result = appointmentService.getAllAppointments(userId, email, includeAdminAppointments);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).email()).isEqualTo(email);
    }
}