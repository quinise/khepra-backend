package com.khepraptah.khepra_site_backend.service;

import com.khepraptah.khepra_site_backend.model.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EventService {
    List<EventDTO> getAllEvents();
    Optional<EventDTO> getEventById(Long id);
    EventDTO saveEvent(EventDTO eventDTO);
    EventDTO updateEvent(Long id, EventDTO EventDTO);
    void deleteEvent(Long id);
}
