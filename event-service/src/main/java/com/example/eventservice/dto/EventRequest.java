package com.example.eventservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EventRequest {

    private String name;
    private LocalDate date;
    private String location;
    private int availableTickets;
    private BigDecimal price;
}