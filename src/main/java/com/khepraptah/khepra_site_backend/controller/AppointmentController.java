package com.khepraptah.khepra_site_backend.controller;

import com.khepraptah.khepra_site_backend.model.AppointmentDTO;
import com.khepraptah.khepra_site_backend.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
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
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) Integer daysRange // optional, for extended control
    ) {
        List<AppointmentDTO> appointments = (userId != null && !userId.isEmpty())
                ? appointmentService.getAppointmentsByUserId(userId)
                : appointmentService.getAllAppointments();

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
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
