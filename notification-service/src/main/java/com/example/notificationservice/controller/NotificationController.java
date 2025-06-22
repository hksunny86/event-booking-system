package com.example.notificationservice.controller;

import com.example.notificationservice.dto.NotificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@Slf4j
public class NotificationController {

    @PostMapping("/send")
    @Operation(summary = "Send booking or cancellation notification")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        try {
            log.info("Processing {} notification", request.getType());

            log.info("Sending {} notification to user={} for event='{}' at location='{}' on {} - tickets={}, amount={}",
                    request.getType(),
                    request.getUserName(),
                    request.getEventName(),
                    request.getEventLocation(),
                    request.getEventDate(),
                    request.getNumberOfTickets(),
                    request.getPaymentAmount()
            );

            // Simulate sending logic (e.g., Kafka, email, SMS etc.)
            return ResponseEntity.ok("Notification sent successfully.");

        } catch (Exception e) {
            log.error("Failed to send notification. Reason: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Failed to send notification.");
        }
    }
}