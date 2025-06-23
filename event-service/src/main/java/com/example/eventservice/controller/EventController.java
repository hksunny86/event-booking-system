package com.example.eventservice.controller;

import com.example.eventservice.dto.EventRequest;
import com.example.eventservice.entity.Event;
import com.example.eventservice.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

    @PostMapping
    @Operation(summary = "Create a new event")
    public ResponseEntity<String> createEvent(@RequestBody EventRequest event) {
        Event savedEvent = eventService.createEvent(event);
        log.info("Event created successfully: ID={}, Name={}", savedEvent.getId(), savedEvent.getName());
        return ResponseEntity
                .created(URI.create("/events/" + savedEvent.getId()))
                .body("Event Created Successfully");
    }

    @GetMapping
    @Operation(summary = "Get all events")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.findAll();
        if (events.isEmpty()) {
            log.warn("No events found");
            return ResponseEntity.noContent().build();
        }
        log.info("Retrieved {} events", events.size());
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventService.findById(id);
        if (event.isPresent()) {
            log.info("Event found: ID={}", id);
            return ResponseEntity.ok(event.get());
        } else {
            log.warn("Event not found: ID={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an event")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody EventRequest eventDetails) {
        Optional<Event> eventOpt = eventService.findById(id);
        if (eventOpt.isEmpty()) {
            log.warn("Event update failed - not found: ID={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Event with ID " + id + " not found");
        }

        Event event = eventOpt.get();
        event.setName(eventDetails.getName());
        event.setDate(eventDetails.getDate());
        event.setLocation(eventDetails.getLocation());
        event.setAvailableTickets(eventDetails.getAvailableTickets());
        event.setPrice(eventDetails.getPrice());

        Event updated = eventService.save(event);
        log.info("Event updated successfully: ID={}", updated.getId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an event")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        if (!eventService.existsById(id)) {
            log.warn("Event delete failed - not found: ID={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Event with ID " + id + " not found");
        }

        eventService.deleteById(id);
        log.info("Event deleted successfully: ID={}", id);
        return ResponseEntity.noContent().build();
    }
}