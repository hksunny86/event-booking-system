# Notification Service

This service logs and simulates sending notifications for ticket bookings and cancellations.

## 🛠 Technologies
- Java 17+
- Spring Boot 3+
- Springdoc OpenAPI
- Lombok

## 🚀 Running the App
```bash
mvn spring-boot:run
```

### Swagger UI
[http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

## 📬 REST Endpoints
| Method | Endpoint               | Description                 |
|--------|-------------------------|-----------------------------|
| POST   | /notifications/send    | Send booking notification   |

## 📨 Example Notification Payload
```json
{
  "eventName": "Concert Night",
  "eventDate": "2025-07-10",
  "eventLocation": "Lahore Expo Center",
  "userName": "Hamza",
  "ticketType": "VIP",
  "numberOfTickets": 2,
  "paymentAmount": "3000.00",
  "type": "CONFIRMATION"
}
```

## 📦 Sample cURL
```bash
curl -X POST http://localhost:8082/notifications/send \
  -H 'Content-Type: application/json' \
  -d '{
        "eventName": "Concert Night",
        "eventDate": "2025-07-10",
        "eventLocation": "Lahore",
        "userName": "Hamza",
        "ticketType": "VIP",
        "numberOfTickets": 2,
        "paymentAmount": "3000.00",
        "type": "CONFIRMATION"
      }'
```

---
