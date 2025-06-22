# Ticket Service

This service handles ticket booking and cancellation for events, and communicates with:
- **Event Service** to fetch event details.
- **Notification Service** to send confirmation/cancellation messages.

## 🛠 Technologies
- Java 17+
- Spring Boot 3+
- Spring Data JPA (H2)
- Springdoc OpenAPI for Swagger
- Lombok
- RestTemplate

## 🚀 Running the App
```bash
mvn spring-boot:run
```

### Swagger UI
[http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

### H2 Console
[http://localhost:8081/h2-console](http://localhost:8081/h2-console)  
**JDBC URL**: `jdbc:h2:mem:ticketdb`  
**Username**: `sa`

## 📬 REST Endpoints
| Method | Endpoint              | Description           |
|--------|------------------------|-----------------------|
| POST   | /tickets/book         | Book a ticket         |
| POST   | /tickets/cancel/{id}  | Cancel a ticket       |

## 🔗 Service Integration
- `GET /events/{id}` → Event Service (http://localhost:8080)
- `POST /notifications/send` → Notification Service (http://localhost:8082)

## 📦 Sample cURL
```bash
curl -X POST http://localhost:8081/tickets/book \
  -H 'Content-Type: application/json' \
  -d '{
        "eventId": 1,
        "userName": "Hamza",
        "ticketType": "VIP",
        "quantity": 2,
        "paymentAmount": 3000.00
      }'
```

## 📄 Postman Collection
Import `event-booking.postman_collection.json` and test all APIs.

---
