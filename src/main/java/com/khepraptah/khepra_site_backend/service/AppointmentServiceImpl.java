package com.khepraptah.khepra_site_backend.service;

import com.khepraptah.khepra_site_backend.model.Appointment;
import com.khepraptah.khepra_site_backend.model.AppointmentDTO;
import com.khepraptah.khepra_site_backend.repository.AppointmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public  AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AppointmentDTO> getAppointmentById(Long id) {
        return appointmentRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = convertToEntity(appointmentDTO);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return convertToDTO(savedAppointment);
    }

    @Override
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow();
        appointment.setType(appointmentDTO.type());
        appointment.setName(appointmentDTO.name());
        appointment.setEmail(appointmentDTO.email());
        appointment.setPhone_number(appointmentDTO.phone_number());
        appointment.setDate(appointmentDTO.date());
        appointment.setIsVirtual(appointmentDTO.isVirtual());
        Appointment updateAppointment = appointmentRepository.save(appointment);
        return convertToDTO(updateAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    // Convert Appointment Entity to AppointmentDTO
    private AppointmentDTO convertToDTO(Appointment appointment) {
        return new AppointmentDTO(appointment.getId(), appointment.getType(), appointment.getUserId(), appointment.getName(), appointment.getEmail(), appointment.getPhone_number(), appointment.getDate(), appointment.getIsVirtual());
    }

    // Convert AppointmentDTO to Appointment Entity
    private Appointment convertToEntity(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        appointment.setType(appointmentDTO.type());
        appointment.setUserId(appointmentDTO.userId());
        appointment.setName(appointmentDTO.name());
        appointment.setEmail(appointmentDTO.email());
        appointment.setPhone_number(appointmentDTO.phone_number());
        appointment.setDate(appointmentDTO.date());
        appointment.setIsVirtual(appointmentDTO.isVirtual());
        return appointment;
    }
}
