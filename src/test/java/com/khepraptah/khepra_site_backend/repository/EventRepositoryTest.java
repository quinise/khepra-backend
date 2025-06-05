package com.khepraptah.khepra_site_backend.repository;

import com.khepraptah.khepra_site_backend.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaRepositories(basePackages = "com.khepraptah.khepra_site_backend.repository")
@EntityScan(basePackages = "com.khepraptah.khepra_site_backend.model")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    private Event virtualEvent;
    private Event inPersonEvent;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();

        // In-Person Event: 14:00 – 15:30
        inPersonEvent = new Event();
        inPersonEvent.setEventName("Workshop");
        inPersonEvent.setStartTime(LocalDateTime.of(2025, 6, 1, 14, 0));
        inPersonEvent.setDuration(90);
        inPersonEvent.setIsVirtual(false);
        inPersonEvent = eventRepository.save(inPersonEvent);

        // Virtual Event: 15:30 – 17:00
        virtualEvent = new Event();
        virtualEvent.setEventName("Zoom Lecture");
        virtualEvent.setStartTime(LocalDateTime.of(2025, 6, 1, 15, 30));
        virtualEvent.setDuration(90);
        virtualEvent.setIsVirtual(true);
        virtualEvent = eventRepository.save(virtualEvent);
    }

    @Test
    void testConflictsIncludeVirtualEvents() {
        LocalDateTime start = LocalDateTime.of(2025, 6, 1, 15, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 1, 16, 0);

        List<Event> conflicts = eventRepository.findConflicts(start, end);

        assertThat(conflicts)
                .extracting(Event::getId)
                .contains(virtualEvent.getId());
    }

    @Test
    void testConflictsIncludeInPersonEvents() {
        LocalDateTime start = LocalDateTime.of(2025, 6, 1, 13, 30);
        LocalDateTime end = LocalDateTime.of(2025, 6, 1, 14, 30);

        List<Event> conflicts = eventRepository.findConflicts(start, end);

        assertThat(conflicts)
                .extracting(Event::getId)
                .contains(inPersonEvent.getId());
    }

    @Test
    void testConflictIncludesBothTypes() {
        LocalDateTime start = LocalDateTime.of(2025, 6, 1, 14, 30);
        LocalDateTime end = LocalDateTime.of(2025, 6, 1, 16, 0);

        List<Event> conflicts = eventRepository.findConflicts(start, end);

        assertThat(conflicts)
                .extracting(Event::getId)
                .contains(inPersonEvent.getId(), virtualEvent.getId());
    }

    @Test
    void testConflictForAdjacentVirtualStartTime() {
        // This range ends exactly when virtualEvent starts — should NOT be a conflict
        LocalDateTime start = LocalDateTime.of(2025, 6, 1, 14, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 1, 15, 30);

        List<Event> conflicts = eventRepository.findConflicts(start, end);

        assertThat(conflicts)
                .extracting(Event::getId)
                .doesNotContain(virtualEvent.getId());
    }

    @Test
    void testNoConflictWhenFullyOutsideRange() {
        LocalDateTime start = LocalDateTime.of(2025, 6, 1, 17, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 1, 18, 0);

        List<Event> conflicts = eventRepository.findConflicts(start, end);

        assertThat(conflicts).isEmpty();
    }
}