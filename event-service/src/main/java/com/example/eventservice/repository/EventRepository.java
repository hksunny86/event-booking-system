package com.example.eventservice.repository;

import com.example.eventservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByDate(LocalDate date);
    List<Event> findByLocationContainingIgnoreCase(String location);
    List<Event> findByNameContainingIgnoreCase(String name);
}