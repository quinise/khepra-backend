package com.khepraptah.khepra_site_backend.repository;

import com.khepraptah.khepra_site_backend.model.Appointment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
    List<Appointment> findByUserId(String userId);

    // Scheduling conflict check
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Appointment a WHERE a.startTime < :end AND a.endTime > :start")
    List<Appointment> findConflicts(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
