# 🎟️ Event Booking System (Microservices Architecture)

This project is a microservices-based **Event Booking System** built using **Spring Boot**. It comprises three services:

- `event-service`: Manages event information (CRUD).
- `ticket-service`: Handles ticket booking/cancellation with resilience and asynchronous notifications.
- `notification-service`: Listens to Kafka topics to send notifications.

Each service is independent, runs on its own port, and communicates via **REST APIs** or **Apache Kafka**.

> 📘 **Note**: Each service contains its **own README.md** file for specific build, run, and API details.

---

## 🧱 Microservices Overview

### 1. `event-service`
- **Tech Stack**: Spring Boot, Spring Data JPA, H2 Database
- **Responsibilities**:
  - Create, read, update, delete (CRUD) events
- **Port**: `8080`
- **Endpoints**:
  - `POST /events` – Create event
  - `GET /events` – Get all events
  - `GET /events/{id}` – Get event by ID
  - `PUT /events/{id}` – Update event
  - `DELETE /events/{id}` – Delete event

---

### 2. `ticket-service`
- **Tech Stack**: Spring Boot, RestTemplate, Kafka Producer, Resilience4j (Circuit Breaker), JPA
- **Responsibilities**:
  - Book and cancel tickets
  - Fetch event info via REST
  - Send notification request via Kafka
  - Apply circuit breaker for fault tolerance
- **Port**: `8081`
- **Endpoints**:
  - `POST /tickets/book` – Book ticket
  - `POST /tickets/cancel/{id}` – Cancel ticket

---

### 3. `notification-service`
- **Tech Stack**: Spring Boot, Kafka Consumer
- **Responsibilities**:
  - Listen to Kafka `notification-topic`
  - Log/send notification
- **Port**: `8082`
- **Endpoints**:
  - `POST /notifications/send` – [Optional] Send notification manually (for testing)

---

## 🛠️ Prerequisites

- Java 17+
- Apache Maven
- IntelliJ IDEA or any Spring Boot-compatible IDE

---

## 🚀 Running the Services

1. **Start Services (via below command):**

   ```bash
   cd event-service
   mvn spring-boot:run

   cd ticket-service
   mvn spring-boot:run

   cd notification-service
   mvn spring-boot:run

