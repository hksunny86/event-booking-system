package com.example.ticketservice.controller;

import com.example.ticketservice.dto.BookTicketRequest;
import com.example.ticketservice.dto.EventResponse;
import com.example.ticketservice.dto.NotificationRequest;
import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.repository.TicketRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketRepository ticketRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${event.service.url:http://localhost:8080}")
    private String eventServiceUrl;

    @Value("${notification.service.url:http://localhost:8082}")
    private String notificationServiceUrl;

    private static final String TICKET_SERVICE = "ticketServiceCB";

    @PostMapping("/book")
    @Operation(summary = "Book a ticket")
    @CircuitBreaker(name = TICKET_SERVICE, fallbackMethod = "bookTicketFallback")
    public ResponseEntity<?> bookTicket(@RequestBody BookTicketRequest request) {
        log.info("Received booking request for eventId={}, user={}", request.getEventId(), request.getUserName());

        try {
            EventResponse event = fetchEventDetails(request.getEventId());
            if (event == null) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Event Service unavailable or returned null");
            }

            if (event.getAvailableTickets() < request.getQuantity()) {
                log.warn("Insufficient tickets for eventId={}, requested={}, available={}",
                        event.getId(), request.getQuantity(), event.getAvailableTickets());
                return ResponseEntity.badRequest().body("Not enough tickets available");
            }

            Ticket ticket = saveBookedTicket(request, event);
            sendNotification(ticket, "CONFIRMATION");

            return ResponseEntity.ok(ticket);

        } catch (Exception e) {
            log.error("Booking failed for eventId={}, error={}", request.getEventId(), e.getMessage(), e);
            return ResponseEntity.internalServerError().body("An error occurred while booking the ticket.");
        }
    }

    @PostMapping("/cancel/{id}")
    @Operation(summary = "Cancel a booked ticket")
    @CircuitBreaker(name = TICKET_SERVICE, fallbackMethod = "cancelTicketFallback")
    public ResponseEntity<?> cancelTicket(@PathVariable Long id) {
        log.info("Received cancellation request for ticketId={}", id);

        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isEmpty()) {
            log.warn("Ticket not found for cancellation: ticketId={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found");
        }

        Ticket ticket = optionalTicket.get();
        ticket.setStatus("CANCELLED");
        ticket = ticketRepository.save(ticket);
        log.info("Ticket cancelled successfully: ticketId={}", ticket.getId());

        sendNotification(ticket, "CANCELLATION");
        return ResponseEntity.ok(ticket);
    }

    // === Utility Methods ===

    private EventResponse fetchEventDetails(Long eventId) {
        try {
            String url = eventServiceUrl + "/events/" + eventId;
            return restTemplate.getForObject(url, EventResponse.class);
        } catch (RestClientException ex) {
            log.error("Failed to fetch event details for eventId={}: {}", eventId, ex.getMessage());
            return null;
        }
    }

    private Ticket saveBookedTicket(BookTicketRequest request, EventResponse event) {
        Ticket ticket = Ticket.builder()
                .eventId(event.getId())
                .eventName(event.getName())
                .eventDate(event.getDate().toString())
                .eventLocation(event.getLocation())
                .userName(request.getUserName())
                .ticketType(request.getTicketType())
                .quantity(request.getQuantity())
                .paymentAmount(request.getPaymentAmount())
                .bookingTime(LocalDateTime.now())
                .status("BOOKED")
                .build();
        ticket = ticketRepository.save(ticket);
        log.info("Ticket booked: ticketId={}, eventId={}", ticket.getId(), event.getId());
        return ticket;
    }

    private void sendNotification(Ticket ticket, String type) {
        NotificationRequest notify = NotificationRequest.builder()
                .eventName(ticket.getEventName())
                .eventDate(ticket.getEventDate())
                .eventLocation(ticket.getEventLocation())
                .userName(ticket.getUserName())
                .ticketType(ticket.getTicketType())
                .numberOfTickets(ticket.getQuantity())
                .paymentAmount(ticket.getPaymentAmount().toString())
                .type(type)
                .build();

        try {
            restTemplate.postForObject(notificationServiceUrl + "/notifications/send", notify, String.class);
            log.info("{} notification sent for ticketId={}", type, ticket.getId());
        } catch (RestClientException e) {
            log.error("Failed to send {} notification for ticketId={}: {}", type, ticket.getId(), e.getMessage());
        }
    }

    // === Fallback Methods ===

    public ResponseEntity<?> bookTicketFallback(BookTicketRequest request, Throwable ex) {
        log.error("Fallback triggered: Book ticket for eventId={} failed: {}", request.getEventId(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Ticket booking service is temporarily unavailable.");
    }

    public ResponseEntity<?> cancelTicketFallback(Long id, Throwable ex) {
        log.error("Fallback triggered: Cancel ticket for ticketId={} failed: {}", id, ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Ticket cancellation service is temporarily unavailable.");
    }
}