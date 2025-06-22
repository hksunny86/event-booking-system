package com.example.eventservice.service;

import com.example.eventservice.entity.Event;
import com.example.eventservice.exception.EventNotFoundException;
import com.example.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository repository;

    public void createEvent(Event event) {
        repository.save(event);
    }

    public List<Event> findAll() {
        return repository.findAll();
    }

    public Optional<Event> findById(Long id) {
        return Optional.ofNullable(repository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + id + " not found")));
    }

    public Event save(Event event) {
        return repository.save(event);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
