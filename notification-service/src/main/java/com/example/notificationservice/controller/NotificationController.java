package com.example.notificationservice.controller;

import com.example.notificationservice.dto.NotificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@Slf4j
public class NotificationController {
    private final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    @PostMapping("/send")
    @Operation(summary = "Send booking or cancellation notification")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        logger.info("Sending {} notification to {} for event {} at {} on {} for {} ticket(s), amount: {}",
                request.getType(),
                request.getUserName(),
                request.getEventName(),
                request.getEventLocation(),
                request.getEventDate(),
                request.getNumberOfTickets(),
                request.getPaymentAmount()
        );
        return ResponseEntity.ok("Notification sent successfully.");
    }
}
