# Event Booking System (Microservices)

This system includes:

- `event-service` (Spring Boot, JPA)
- `ticket-service` (RestTemplate + Resilience4j)
- `notification-service` (Kafka consumer)

Each microservice runs independently and communicates over REST or Kafka.
