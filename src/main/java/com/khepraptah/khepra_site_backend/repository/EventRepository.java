package com.khepraptah.khepra_site_backend.repository;

import com.khepraptah.khepra_site_backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{}
