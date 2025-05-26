package com.khepraptah.khepra_site_backend.repository;

import com.khepraptah.khepra_site_backend.model.Event;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Event e WHERE (:start < e.endTime AND :end > e.startTime)")
    List<Event> findConflicts(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
