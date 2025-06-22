package com.example.ticketservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;
    private String eventName;
    private String eventDate;
    private String eventLocation;

    private String userName;
    private String ticketType;
    private int quantity;
    private BigDecimal paymentAmount;

    private String status; // BOOKED / CANCELLED
    private LocalDateTime bookingTime;
}