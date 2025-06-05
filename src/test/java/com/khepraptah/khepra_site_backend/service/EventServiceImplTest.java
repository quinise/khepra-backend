package com.khepraptah.khepra_site_backend.service;

import com.khepraptah.khepra_site_backend.exception.BadRequestException;
import com.khepraptah.khepra_site_backend.model.Event;
import com.khepraptah.khepra_site_backend.model.EventDTO;
import com.khepraptah.khepra_site_backend.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceImplTest {

    private EventRepository eventRepository;
    private ScheduleConflictService scheduleConflictService;
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        eventRepository = Mockito.mock(EventRepository.class);
        scheduleConflictService = Mockito.mock(ScheduleConflictService.class);
        eventService = new EventServiceImpl(eventRepository, scheduleConflictService);
    }

    // Utility method to convert LocalDate to java.util.Date
    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private EventDTO buildValidEventDTO() {
        LocalDate startLocalDate = LocalDate.now();
        LocalDate endLocalDate = LocalDate.now();

        return new EventDTO(
                1L,
                "Test Event",
                "WORKSHOP",
                "Client A",
                toDate(startLocalDate),
                toDate(endLocalDate),
                90,
                LocalDateTime.of(2025, 6, 10, 14, 0),
                LocalDateTime.of(2025, 6, 10, 15, 30),
                "123 Main St",
                "Seattle",
                "WA",
                98101.0,
                "Description here",
                false
        );
    }

    private Event buildValidEvent() {
        Event event = new Event();
        event.setId(1L);
        event.setEventName("Test Event");
        event.setEventType("WORKSHOP");
        event.setClientName("Client A");
        event.setStartDate(toDate(LocalDate.now()));
        event.setEndDate(toDate(LocalDate.now()));
        event.setStartTime(LocalDateTime.of(2025, 6, 10, 14, 0));
        event.setEndTime(LocalDateTime.of(2025, 6, 10, 15, 30));
        event.setDuration(90);
        event.setStreetAddress("123 Main St");
        event.setCity("Seattle");
        event.setState("WA");
        event.setZipCode(98101.0);
        event.setDescription("Description here");
        event.setIsVirtual(false);
        return event;
    }

    @Test
    void testGetAllEvents() {
        Event event1 = buildValidEvent();
        Event event2 = buildValidEvent();
        event2.setId(2L);
        event2.setEventName("Another Event");

        when(eventRepository.findAll()).thenReturn(List.of(event1, event2));

        List<EventDTO> events = eventService.getAllEvents();

        assertEquals(2, events.size());
        assertEquals("Test Event", events.get(0).getEventName());
        assertEquals("Another Event", events.get(1).getEventName());
    }

    @Test
    void testGetEventByIdFound() {
        Event event = buildValidEvent();
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<EventDTO> eventDTO = eventService.getEventById(1L);

        assertTrue(eventDTO.isPresent());
        assertEquals("Test Event", eventDTO.get().getEventName());
    }

    @Test
    void testGetEventByIdNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<EventDTO> eventDTO = eventService.getEventById(1L);

        assertFalse(eventDTO.isPresent());
    }

    @Test
    void testSaveEventSuccess() {
        EventDTO dto = buildValidEventDTO();
        Event event = buildValidEvent();

        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventDTO savedDTO = eventService.saveEvent(dto);

        verify(scheduleConflictService, times(1)).checkForConflicts(any(Event.class));
        assertEquals(dto.getEventName(), savedDTO.getEventName());
    }

    @Test
    void testSaveEventInvalidDuration() {
        EventDTO dto = buildValidEventDTO();
        // Make duration invalid by setting endTime before startTime
        EventDTO invalidDurationDto = new EventDTO(
                dto.getId(),
                dto.getEventName(),
                dto.getEventType(),
                dto.getClientName(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getDurationMinutes(),
                dto.getStartTime(),
                dto.getStartTime().minusMinutes(10), // endTime before startTime
                dto.getStreetAddress(),
                dto.getCity(),
                dto.getState(),
                dto.getZipCode(),
                dto.getDescription(),
                dto.getIsVirtual()
        );

        assertThrows(BadRequestException.class, () -> eventService.saveEvent(invalidDurationDto));
    }

    @Test
    void testUpdateEventSuccess() {
        EventDTO dto = buildValidEventDTO();
        Event existingEvent = buildValidEvent();

        when(eventRepository.findById(dto.getId())).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenAnswer(i -> i.getArgument(0));

        EventDTO updatedDTO = eventService.updateEvent(dto.getId(), dto);

        verify(scheduleConflictService, times(1)).checkForConflicts(any(Event.class));
        assertEquals(dto.getEventName(), updatedDTO.getEventName());
    }

    @Test
    void testUpdateEventNotFoundThrows() {
        EventDTO dto = buildValidEventDTO();

        when(eventRepository.findById(dto.getId())).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () -> eventService.updateEvent(dto.getId(), dto));
    }

    @Test
    void testDeleteEvent() {
        Long id = 1L;
        doNothing().when(eventRepository).deleteById(id);

        eventService.deleteEvent(id);

        verify(eventRepository, times(1)).deleteById(id);
    }
}