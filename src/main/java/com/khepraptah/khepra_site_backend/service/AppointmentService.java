package com.khepraptah.khepra_site_backend.service;

import com.khepraptah.khepra_site_backend.model.AppointmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AppointmentService {
    List<AppointmentDTO> getAllAppointments(String userId, String email, boolean includeAdminAppointments);
    Optional<AppointmentDTO> getAppointmentById(Long id);
    AppointmentDTO saveAppointment(AppointmentDTO appointmentDTO);
    AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO);
    void deleteAppointment(Long id);
    List<AppointmentDTO> getAppointmentsByUserId(String userId);
}
