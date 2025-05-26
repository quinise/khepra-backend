package com.khepraptah.khepra_site_backend.service;

import com.khepraptah.khepra_site_backend.model.Event;
import com.khepraptah.khepra_site_backend.model.EventDTO;
import com.khepraptah.khepra_site_backend.model.EventType;
import com.khepraptah.khepra_site_backend.repository.EventRepository;
import com.khepraptah.khepra_site_backend.service.ScheduleConflictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ScheduleConflictService scheduleConflictService;

    public EventServiceImpl(EventRepository eventRepository, ScheduleConflictService scheduleConflictService) {
        this.eventRepository = eventRepository;
        this.scheduleConflictService = scheduleConflictService;
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

    @Transactional
    public EventDTO saveEvent(EventDTO eventDTO) {
        Event event = convertToEntity(eventDTO);
        event.calculateEndTime();
        scheduleConflictService.checkForConflicts(event);
        Event savedEvent = eventRepository.save(event);
        return convertToDTO(savedEvent);
    }

    @Transactional
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        Event event = eventRepository.findById(id).orElseThrow();
        event.setEventName(eventDTO.eventName());
        event.setEventType(eventDTO.eventType());
        event.setClientName(eventDTO.clientName());
        event.setStartDate(eventDTO.startDate());
        event.setStartTime(eventDTO.startTime());

        try {
            EventType appointmentType =EventType.valueOf(eventDTO.eventType().toUpperCase());
            event.setDuration(appointmentType.getDurationMinutes());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid appointment type: " + eventDTO.eventType());
        }

        event.setEndTime(eventDTO.endTime());
        event.setStreetAddress(eventDTO.streetAddress());
        event.setCity(eventDTO.city());
        event.setState(eventDTO.state());
        event.setZipCode(eventDTO.zipCode());
        event.setDescription(eventDTO.description());
        event.setIsVirtual(eventDTO.isVirtual());

        scheduleConflictService.checkForConflicts(event);

        Event updateEvent = eventRepository.save(event);
        return convertToDTO(updateEvent);
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    private EventDTO convertToDTO(Event event) {
        return new EventDTO(
                event.getId(),
                event.getEventName(),
                event.getEventType(),
                event.getClientName(),
                event.getStartDate(),
                event.getEndDate(),
                event.getDuration(),
                event.getStartTime(),
                event.getEndTime(),
                event.getStreetAddress(),
                event.getCity(),
                event.getState(),
                event.getZipCode(),
                event.getDescription(),
                event.getIsVirtualRaw()
        );
    }

    private Event convertToEntity(EventDTO eventDTO) {
        Event event = new Event();
        event.setId(eventDTO.id());
        event.setEventName(eventDTO.eventName());
        event.setEventType(eventDTO.eventType());
        event.setClientName(eventDTO.clientName());
        event.setStartDate(eventDTO.startDate());
        event.setDuration(eventDTO.durationMinutes());
        event.setStartTime(eventDTO.startTime());
        event.setEndTime(eventDTO.endTime());
        event.setStreetAddress(eventDTO.streetAddress());
        event.setCity(eventDTO.city());
        event.setState(eventDTO.state());
        event.setZipCode(eventDTO.zipCode());
        event.setDescription(eventDTO.description());
        event.setIsVirtual(eventDTO.isVirtual());
        return event;
    }
}
