package com.khepraptah.khepra_site_backend.repository;

import com.khepraptah.khepra_site_backend.model.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaRepositories(basePackages = "com.khepraptah.khepra_site_backend.repository")
@EntityScan(basePackages = "com.khepraptah.khepra_site_backend.model")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Appointment appointment1;
    private Appointment appointment2;

    @BeforeEach
    void setUp() {
        appointmentRepository.deleteAll();

        appointment1 = new Appointment();
        appointment1.setUserId("user1");
        appointment1.setStartTime(LocalDateTime.of(2025, 6, 1, 10, 0));
        appointment1.setEndTime(LocalDateTime.of(2025, 6, 1, 11, 0));
        appointmentRepository.save(appointment1);

        appointment2 = new Appointment();
        appointment2.setUserId("user2");
        appointment2.setStartTime(LocalDateTime.of(2025, 6, 1, 11, 0));
        appointment2.setEndTime(LocalDateTime.of(2025, 6, 1, 12, 0));
        appointmentRepository.save(appointment2);

        Appointment adminAppointment = new Appointment();
        adminAppointment.setEmail("admin@test.com");
        adminAppointment.setCreatedByAdmin(true);
        adminAppointment.setUserId("someUserId");
        adminAppointment.setDate(Date.from(LocalDate.of(2025, 6, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        adminAppointment.setStartTime(LocalDateTime.of(2025, 6, 1, 10, 0));
        adminAppointment.setEndTime(LocalDateTime.of(2025, 6, 1, 10, 30));
        adminAppointment.setDuration(30);
        adminAppointment.setType("READING");
        adminAppointment.setIsVirtual(true);

        appointmentRepository.save(adminAppointment);
    }

    @Test
    void testFindByUserId() {
        List<Appointment> user1Appointments = appointmentRepository.findByUserId("user1");
        assertThat(user1Appointments).hasSize(1);
        assertThat(user1Appointments.get(0).getUserId()).isEqualTo("user1");

        List<Appointment> user2Appointments = appointmentRepository.findByUserId("user2");
        assertThat(user2Appointments).hasSize(1);
        assertThat(user2Appointments.get(0).getUserId()).isEqualTo("user2");

        List<Appointment> noAppointments = appointmentRepository.findByUserId("unknown");
        assertThat(noAppointments).isEmpty();
    }

    @Test
    void testFindAdminCreatedAppointmentsByUserId() {
        List<Appointment> result = appointmentRepository.findAdminCreatedAppointmentsByUserId("someUserId");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCreatedByAdmin()).isTrue();
    }

    @Test
    void testFindAdminCreatedAppointmentsByEmail() {
        List<Appointment> result = appointmentRepository.findAdminCreatedAppointmentsByEmail("admin@test.com");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCreatedByAdmin()).isTrue();
    }

    @Test
    void testFindConflictsReturnsConflictingAppointments() {
        LocalDateTime conflictStart = LocalDateTime.of(2025, 6, 1, 10, 30);
        LocalDateTime conflictEnd = LocalDateTime.of(2025, 6, 1, 11, 30);

        List<Appointment> conflicts = appointmentRepository.findConflicts(conflictStart, conflictEnd);

        assertThat(conflicts).isNotEmpty();
        assertThat(conflicts).contains(appointment1);
        assertThat(conflicts).containsExactlyInAnyOrder(appointment1, appointment2);
    }

    @Test
    void testFindConflictsReturnsEmptyWhenNoOverlap() {
        LocalDateTime noConflictStart = LocalDateTime.of(2025, 6, 1, 12, 0);
        LocalDateTime noConflictEnd = LocalDateTime.of(2025, 6, 1, 13, 0);

        List<Appointment> conflicts = appointmentRepository.findConflicts(noConflictStart, noConflictEnd);

        assertThat(conflicts).isEmpty();
    }
}
