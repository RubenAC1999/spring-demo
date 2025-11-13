# Task Manager API - Spring Boot (Employees <-> Tasks <-> Projects) (In development)

Simple API Rest developed using Spring Boot, Spring Data JPA and PostgreSQL to manage employees, tasks and projects.
This includes entity relationships, DTOs, validations, automatic audit, centralized handling exception and layered architecture.

Project to learn Spring and to implement a clean architecture and good practises in Java backend.

-- 

## Technologies used

- **Java 17**
- **Spring Boot 3**
  - Spring Web
  - Spring Data JPA
  - Spring Validation
  - Spring Security
- **Hibernate JPA**
- **PostgreSQL**
- **Lombok**
- **Maven**

## Project architecture (Layered)

src/main/java/com.example.demoPersonal

├─ controller -> REST controllers

├─ service -> Business logic

├─ repository -> Data

├─ entity -> JPA Entities

├─ dto -> DTOs (request/response)

├─ exception -> Exceptions

├─ config -> Configuration

├─ mapper -> Entity Conversion <-> DTO



