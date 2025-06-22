//package com.example.notificationservice.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class NotificationConsumer {
//
//    @KafkaListener(topics = "ticket-notifications", groupId = "notification-group")
//    public void listen(String message) {
//        log.info("Received Kafka notification: {}", message);
//
//        // Simulate sending email or push notification
//        System.out.println("Sending notification: " + message);
//    }
//}
