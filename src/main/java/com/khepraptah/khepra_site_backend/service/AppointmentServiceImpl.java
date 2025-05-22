package com.khepraptah.khepra_site_backend.service;

import com.khepraptah.khepra_site_backend.model.Appointment;
import com.khepraptah.khepra_site_backend.model.AppointmentDTO;
import com.khepraptah.khepra_site_backend.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
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


    @Override
    public AppointmentDTO saveAppointment(AppointmentDTO dto) {
        Appointment saved = appointmentRepository.save(convertToEntity(dto));
        return convertToDto(saved);
    }

    @Override
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO dto) {
        Optional<Appointment> optional = appointmentRepository.findById(id);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Appointment not found");
        }
        Appointment updated = convertToEntity(dto);
        updated.setId(id);
        return convertToDto(appointmentRepository.save(updated));
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
                appointment.getIsVirtual()
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
        appointment.setIsVirtual(appointmentDTO.isVirtual());
        return appointment;
    }
}
