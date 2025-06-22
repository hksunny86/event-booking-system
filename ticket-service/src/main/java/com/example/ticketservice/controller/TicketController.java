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
        try {
            // 1. Call Event Service
            String eventUrl = eventServiceUrl + "/events/" + request.getEventId();
            EventResponse event = restTemplate.getForObject(eventUrl, EventResponse.class);

            if (event == null) {
                return ResponseEntity.status(502).body("Event Service unavailable or returned null");
            }

            if (event.getAvailableTickets() < request.getQuantity()) {
                return ResponseEntity.badRequest().body("Not enough tickets available");
            }

            // 2. Save ticket
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

            // 3. Notify user
            NotificationRequest notify = NotificationRequest.builder()
                    .eventName(event.getName())
                    .eventDate(event.getDate().toString())
                    .eventLocation(event.getLocation())
                    .userName(ticket.getUserName())
                    .ticketType(ticket.getTicketType())
                    .numberOfTickets(ticket.getQuantity())
                    .paymentAmount(ticket.getPaymentAmount().toString())
                    .type("CONFIRMATION")
                    .build();

            try {
                restTemplate.postForObject(notificationServiceUrl + "/notifications/send", notify, String.class);
            } catch (RestClientException e) {
                log.error("Failed to notify user after booking. Reason: {}", e.getMessage());
            }

            return ResponseEntity.ok(ticket);

        } catch (Exception e) {
            log.error("Unexpected error occurred while booking ticket", e);
            return ResponseEntity.internalServerError().body("Error occurred while booking ticket");
        }
    }

    @PostMapping("/cancel/{id}")
    @Operation(summary = "Cancel a booked ticket")
    @CircuitBreaker(name = TICKET_SERVICE, fallbackMethod = "cancelTicketFallback")
    public ResponseEntity<?> cancelTicket(@PathVariable Long id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Ticket ticket = optionalTicket.get();
        ticket.setStatus("CANCELLED");
        ticket = ticketRepository.save(ticket);

        NotificationRequest notify = NotificationRequest.builder()
                .eventName(ticket.getEventName())
                .eventDate(ticket.getEventDate())
                .eventLocation(ticket.getEventLocation())
                .userName(ticket.getUserName())
                .ticketType(ticket.getTicketType())
                .numberOfTickets(ticket.getQuantity())
                .paymentAmount(ticket.getPaymentAmount().toString())
                .type("CANCELLATION")
                .build();

        try {
            restTemplate.postForObject(notificationServiceUrl + "/notifications/send", notify, String.class);
        } catch (RestClientException e) {
            log.error("Failed to notify user after cancellation. Reason: {}", e.getMessage());
        }

        return ResponseEntity.ok(ticket);
    }

    //Fallbacks
    public ResponseEntity<?> bookTicketFallback(BookTicketRequest request, Throwable ex) {
        log.error("Book Ticket fallback triggered: {}", ex.getMessage());
        return ResponseEntity.status(503).body("Service temporarily unavailable, please try again later.");
    }

    public ResponseEntity<?> cancelTicketFallback(Long id, Throwable ex) {
        log.error("Cancel Ticket fallback triggered: {}", ex.getMessage());
        return ResponseEntity.status(503).body("Service temporarily unavailable, please try again later.");
    }
}