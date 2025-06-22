package com.khepraptah.khepra_site_backend.service;

import com.khepraptah.khepra_site_backend.model.Appointment;
import com.khepraptah.khepra_site_backend.model.AppointmentDTO;
import com.khepraptah.khepra_site_backend.model.AppointmentType;
import com.khepraptah.khepra_site_backend.repository.AppointmentRepository;
import com.khepraptah.khepra_site_backend.service.ScheduleConflictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final ScheduleConflictService scheduleConflictService;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, ScheduleConflictService scheduleConflictService) {
        this.appointmentRepository = appointmentRepository;
        this.scheduleConflictService = scheduleConflictService;
    }

    @Override
    public List<AppointmentDTO> getAllAppointments(String userId, String email, boolean includeAdminAppointments) {
        if (userId == null && email == null) {
            throw new IllegalArgumentException("Either userId or email must be provided.");
        }

        List<Appointment> appointments = new ArrayList<>();

        // Fetch appointments based on userId or email
        if (userId != null && !userId.isEmpty()) {
            appointments = appointmentRepository.findByUserId(userId);
        } else if (email != null && !email.isEmpty()) {
            appointments = appointmentRepository.findByEmail(email);
        }

        // If admin-created appointments should be included, fetch those too
        if (includeAdminAppointments) {
            List<Appointment> adminAppointments;
            if (userId != null && !userId.isEmpty()) {
                adminAppointments = appointmentRepository.findAdminCreatedAppointmentsByUserId(userId);
            } else if (email != null && !email.isEmpty()) {
                adminAppointments = appointmentRepository.findAdminCreatedAppointmentsByEmail(email);
            } else {
                adminAppointments = new ArrayList<>();
            }

            appointments.addAll(adminAppointments);
        }

        // Convert to DTO and return the result
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AppointmentDTO> getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public List<AppointmentDTO> getAppointmentsByUserId(String userId) {
        return appointmentRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getUpcomingAppointmentsByUserId(String userId) {
        List<AppointmentDTO> allAppointments = getAppointmentsByUserId(userId);
        LocalDateTime now = LocalDateTime.now();

        return allAppointments.stream()
                .filter(appointment -> {
                    LocalDateTime appointmentDate = appointment.getDate();
                    return appointmentDate != null && appointmentDate.isAfter(now);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentDTO saveAppointment(AppointmentDTO dto) {
        Appointment appointment = convertToEntity(dto);
        appointment.calculateEndTime();
        scheduleConflictService.checkForConflicts(appointment);

        Appointment saved = appointmentRepository.save(appointment);
        return convertToDto(saved);
    }

    @Transactional
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO dto) {
        Optional<Appointment> optional = appointmentRepository.findById(id);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Appointment not found");
        }

        Appointment updated = convertToEntity(dto);
        updated.setId(id); // Make sure to preserve the existing ID

        scheduleConflictService.checkForConflicts(updated);

        Appointment saved = appointmentRepository.save(updated);
        return convertToDto(saved);
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    // ---- Conversion Methods ----
    private AppointmentDTO convertToDto(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getType(),
                appointment.getUserId(),
                appointment.getName(),
                appointment.getEmail(),
                appointment.getPhoneNumber(),
                appointment.getDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                appointment.getDuration(),
                appointment.getStreetAddress(),
                appointment.getCity(),
                appointment.getState(),
                appointment.getZipCode(),
                appointment.getIsVirtualRaw(),
                appointment.getCreatedByAdmin()
        );
    }

    private Appointment convertToEntity(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        appointment.setId(appointmentDTO.id());
        appointment.setType(appointmentDTO.type());
        appointment.setUserId(appointmentDTO.userId());
        appointment.setName(appointmentDTO.name());
        appointment.setEmail(appointmentDTO.email());
        appointment.setPhoneNumber(appointmentDTO.phoneNumber());
        appointment.setDate(appointmentDTO.date());
        appointment.setStartTime(appointmentDTO.startTime());
        appointment.setEndTime(appointment.getStartTime().plusMinutes(appointment.getDuration()));
        try {
            AppointmentType appointmentType = AppointmentType.valueOf(appointmentDTO.type().toUpperCase());
            appointment.setDuration(appointmentType.getDurationMinutes());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid appointment type: " + appointmentDTO.type());
        }

        appointment.setStreetAddress(appointmentDTO.streetAddress());
        appointment.setCity(appointmentDTO.city());
        appointment.setState(appointmentDTO.state());
        appointment.setZipCode(appointmentDTO.zipCode());
        appointment.setIsVirtual(appointmentDTO.isVirtual());
        appointment.setCreatedByAdmin(appointmentDTO.createdByAdmin());
        return appointment;
    }
}
