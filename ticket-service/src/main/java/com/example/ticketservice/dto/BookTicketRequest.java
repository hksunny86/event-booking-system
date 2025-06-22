package com.example.ticketservice.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookTicketRequest {
    private Long eventId;
    private String userName;
    private String ticketType;
    private int quantity;
    private BigDecimal paymentAmount;
}