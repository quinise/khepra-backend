package com.khepraptah.khepra_site_backend.controller;

import com.khepraptah.khepra_site_backend.model.AppointmentDTO;
import com.khepraptah.khepra_site_backend.service.AppointmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Vector;
import java.net.URI;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAppointments(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) Integer daysRange, // optional, for extended control
            @RequestParam(defaultValue = "false") Boolean includeAdminAppointments
    ) {
        List<AppointmentDTO> appointments;

        if (userId == null && email == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        appointments = appointmentService.getAllAppointments(userId, email, includeAdminAppointments);

        LocalDateTime now = LocalDateTime.now();

        if ("past".equalsIgnoreCase(filter)) {
            appointments = appointments.stream()
                    .filter(a -> a.getDate() != null && a.getDate().isBefore(now))
                    .toList();
        } else if ("upcoming".equalsIgnoreCase(filter)) {
            appointments = appointments.stream()
                    .filter(a -> a.getDate() != null && a.getDate().isAfter(now))
                    .toList();
        }

        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        Optional<AppointmentDTO> appointment = appointmentService.getAppointmentById(id);
        return appointment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO created = appointmentService.saveAppointment(appointmentDTO);
        URI location = URI.create("/api/appointments/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO) {
        try {
            AppointmentDTO updatedAppointment = appointmentService.updateAppointment(id, appointmentDTO);
            return ResponseEntity.ok(updatedAppointment);
        } catch (IllegalArgumentException e) {
            // This likely means appointment not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // For other exceptions, return 500 error or handle accordingly
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
