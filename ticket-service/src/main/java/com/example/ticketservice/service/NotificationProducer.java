//package com.example.ticketservice.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class NotificationProducer {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public void sendNotification(String message) {
//        kafkaTemplate.send("ticket-notifications", message);
//    }
//}
