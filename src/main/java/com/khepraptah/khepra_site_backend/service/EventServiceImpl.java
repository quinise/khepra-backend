package com.khepraptah.khepra_site_backend.service;

import com.khepraptah.khepra_site_backend.exception.BadRequestException;
import com.khepraptah.khepra_site_backend.model.Event;
import com.khepraptah.khepra_site_backend.model.EventDTO;
import com.khepraptah.khepra_site_backend.model.EventType;
import com.khepraptah.khepra_site_backend.repository.EventRepository;
import com.khepraptah.khepra_site_backend.service.ScheduleConflictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
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
        validateEvent(eventDTO);
        Event event = convertToEntity(eventDTO);
        event.calculateEndTime();
        scheduleConflictService.checkForConflicts(event);
        Event savedEvent = eventRepository.save(event);
        return convertToDTO(savedEvent);
    }

    private void validateEvent(EventDTO eventDTO) {
        LocalDateTime start = eventDTO.getStartTime();
        LocalDateTime end = eventDTO.getEndTime();

        if (start == null || end == null) {
            throw new BadRequestException("Start and end time must be provided.");
        }

        if (!start.isBefore(end)) {
            throw new BadRequestException("Start time must be before end time.");
        }

        long durationMinutes = Duration.between(start, end).toMinutes();
        EventType type = EventType.valueOf(eventDTO.getEventType().toUpperCase());
        long minDuration = getMinDuration(type);

        if (durationMinutes < minDuration) {
            throw new BadRequestException(
                    String.format("Event of type %s must be at least %d minutes long.", eventDTO.getEventType(), minDuration)
            );
        }
    }

    private long getMinDuration(EventType type) {
        return switch (type) {
            case WORKSHOP -> 90;
            case BEMBE -> 120;
            case LECTURE -> 90;
            case EGUNGUN -> 60;
            case TRAINING -> 120;
            default -> 30; // fallback for untyped events
        };
    }

    @Transactional
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        Event event = eventRepository.findById(id).orElseThrow();
        event.setEventName(eventDTO.getEventName());
        event.setEventType(eventDTO.getEventType());
        event.setClientName(eventDTO.getClientName());
        event.setStartDate(eventDTO.getStartDate());
        event.setStartTime(eventDTO.getStartTime());

        try {
            EventType appointmentType =EventType.valueOf(eventDTO.getEventType().toUpperCase());
            event.setDuration(appointmentType.getDurationMinutes());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid appointment type: " + eventDTO.getEventType());
        }

        event.setEndTime(eventDTO.getEndTime());
        event.setStreetAddress(eventDTO.getStreetAddress());
        event.setCity(eventDTO.getCity());
        event.setState(eventDTO.getState());
        event.setZipCode(eventDTO.getZipCode());
        event.setDescription(eventDTO.getDescription());
        event.setIsVirtual(eventDTO.getIsVirtual());

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
        event.setId(eventDTO.getId());
        event.setEventName(eventDTO.getEventName());
        event.setEventType(eventDTO.getEventType());
        event.setClientName(eventDTO.getClientName());
        event.setStartDate(eventDTO.getStartDate());
        event.setDuration(eventDTO.getDurationMinutes());
        event.setStartTime(eventDTO.getStartTime());
        event.setEndTime(eventDTO.getEndTime());
        event.setStreetAddress(eventDTO.getStreetAddress());
        event.setCity(eventDTO.getCity());
        event.setState(eventDTO.getState());
        event.setZipCode(eventDTO.getZipCode());
        event.setDescription(eventDTO.getDescription());
        event.setIsVirtual(eventDTO.getIsVirtual());
        return event;
    }
}
