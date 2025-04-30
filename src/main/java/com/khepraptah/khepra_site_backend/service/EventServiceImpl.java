package com.khepraptah.khepra_site_backend.service;

import com.khepraptah.khepra_site_backend.model.Event;
import com.khepraptah.khepra_site_backend.model.EventDTO;
import com.khepraptah.khepra_site_backend.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EventDTO> getEventById(Long id) {
        return eventRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public EventDTO saveEvent(EventDTO eventDTO) {
        Event event = convertToEntity(eventDTO);
        Event savedEvent = eventRepository.save(event);
        return convertToDTO(savedEvent);
    }

    @Override
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        Event event = eventRepository.findById(id).orElseThrow();
        event.setEventName(eventDTO.eventName());
        event.setEventType(eventDTO.eventType());
        event.setClientName(eventDTO.clientName());
        event.setStartDate(eventDTO.startDate());
        event.setEndDate(eventDTO.endDate());
        event.setStreetAddress(eventDTO.streetAddress());
        event.setCity(eventDTO.city());
        event.setState(eventDTO.state());
        event.setZipCode(eventDTO.zipCode());
        event.setDescription(eventDTO.description());
        event.setIsVirtual(eventDTO.isVirtual());
        Event updateEvent = eventRepository.save(event);
        return convertToDTO(updateEvent);
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    private EventDTO convertToDTO(Event event) {
        return new EventDTO(event.getId(), event.getEventName(), event.getEventType(), event.getClientName(), event.getStartDate(), event.getEndDate(), event.getStreetAddress(), event.getCity(), event.getState(), event.getZipCode(), event.getDescription(), event.getIsVirtual());
    }

    private Event convertToEntity(EventDTO eventDTO) {
        Event event = new Event();
        event.setEventName(eventDTO.eventName());
        event.setEventType(eventDTO.eventType());
        event.setClientName(eventDTO.clientName());
        event.setStartDate(eventDTO.startDate());
        event.setEndDate(eventDTO.endDate());
        event.setStreetAddress(eventDTO.streetAddress());
        event.setCity(eventDTO.city());
        event.setState(eventDTO.state());
        event.setZipCode(eventDTO.zipCode());
        event.setDescription(eventDTO.description());
        event.setIsVirtual(eventDTO.isVirtual());
        return event;
    }
}
