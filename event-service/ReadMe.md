/* Event Service - Spring Boot (event-service) */

// README.md
# Event Service

This service manages the creation, retrieval, updating, and deletion of events.

## 🛠 Technologies
- Java 17+
- Spring Boot 3+
- Spring Data JPA (H2)
- Springdoc OpenAPI for Swagger
- Lombok

## 🚀 Running the App
```bash
mvn spring-boot:run
```

### Swagger UI
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### H2 Console
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
**JDBC URL**: `jdbc:h2:mem:eventdb`  
**Username**: `sa`

## 📬 REST Endpoints
| Method | Endpoint       | Description         |
|--------|----------------|---------------------|
| POST   | /events        | Create new event    |
| GET    | /events        | List all events     |
| GET    | /events/{id}   | Get event by ID     |
| PUT    | /events/{id}   | Update event        |
| DELETE | /events/{id}   | Delete event        |

## 📦 Sample cURL
```bash
curl -X POST http://localhost:8080/events \
  -H 'Content-Type: application/json' \
  -d '{
        "name": "Concert",
        "date": "2025-07-10",
        "location": "Lahore",
        "availableTickets": 100,
        "price": 1500.00
      }'
```

## 📄 Postman Collection
Import `event-booking.postman_collection.json` and test all APIs.

---
