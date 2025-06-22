package com.example.notificationservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NotificationRequest {
    private String eventName;
    private String eventDate;
    private String eventLocation;
    private String userName;
    private String ticketType;
    private int numberOfTickets;
    private String paymentAmount;
    private String type; // CONFIRMATION or CANCELLATION
}