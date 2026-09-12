# Smart Parking System

A Spring Boot backend for a smart parking lot that supports:
- vehicle check-in and check-out
- parking spot assignment based on vehicle type
- real-time availability tracking
- parking fee calculation
- H2 in-memory database for local development

## Tech stack
- Java 21
- Spring Boot 3.3.4
- Spring Data JPA
- H2 Database
- Maven

## Low-level design summary

### 1. Data model
Entities:
- `Vehicle`: stores license plate and vehicle type.
- `ParkingSpot`: stores floor, spot number, compatible vehicle type, availability.
- `ParkingTransaction`: links a vehicle to a spot and stores check-in, check-out, fee, status.

### 2. Spot allocation algorithm
The system matches the incoming vehicle type to the first available spot of the same category:
- Motorcycle -> motorcycle slots
- Car -> car slots
- Bus -> bus slots

This is a simple but efficient rule for a low-level parking system when each slot has a defined vehicle class.

### 3. Fee calculation logic
Fee is computed by charging the hourly rate for each complete hour, with a minimum charge of one hour for any positive stay.

Example rates:
- Motorcycle: 40 per hour
- Car: 80 per hour
- Bus: 120 per hour

### 4. Concurrency handling
The backend uses Spring-managed transactional service patterns and JPA repositories. In production, this could be extended with:
- optimistic locking on `ParkingSpot`
- database row locks for spot reservation
- synchronized reservation flow for multi-threaded entry/exit processing

## API examples

### Check in
`POST /api/vehicles/checkin?licensePlate=KA01AB1234&vehicleType=CAR`

### Check out
`POST /api/vehicles/checkout?licensePlate=KA01AB1234`

### Spots
`GET /api/spots`

## H2 console
Open:
- http://localhost:8080/h2-console

JDBC URL:
- `jdbc:h2:mem:smartparking`

Username:
- `sa`

Password:
- empty

## Run locally
```bash
mvn spring-boot:run
```

## Test
```bash
mvn test
```
