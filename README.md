# Task Manager API 
Task Manager API to manage employees, tasks and projects.


## Table of Contents

- [Description](#description)
- [Project Goals](#project-goals)
- [Main Features](#main-features)
- [Technologies](#technologies)
- [Installation and Configuration](#installation-and-configuration)
- [Run with Docker](#run-with-docker)
- [Usage](#usage)
- [Project Architecture](#project-architecture)
- [Roadmap](#roadmap)
- [Author](#author)

---

## Description
Task Manager API built with Spring Boot following clean architecture and industry best practices.
This project focuses on backend development concepts such security, persistence, testing and good API design.

---

## Project Goals

The main purpose of this is apply the best practices while simulating a real-world application.

- Designing a clean and maintainable REST API
- Applying layered architecture principles (Controller, Service, Repository)
- Implementing authentication and authorization using JWT
- Managing persistence with JPA and PostgreSQL
- Handling database versioning with Flyway
- Applying centralized exception handling
- Implementing role-based access control
- Writing unit tests for business logic
- Preparing the application for containerized deployment using Docker

---

## Main features
- CRUD with employees, tasks and projects following business rules.
- Assign and unassign tasks to employees and to projects.
- Authentication using email and password (Generating a JWT)
- Password encoded in the database.

---

## Technologies
- Java 17
- Spring boot 3
- Spring Security (Authentication and authorization)
- JPA/Hibernate (interact with the DB)
- PostgreSQL (Relational DB)
- JUnit and Mockito (Unit tests)
- Flyway (DB Migrations)
- Docker (Containerization)

---

## Installation and configuration



1. Cloning the repository

```bash
git clone https://github.com/RubenAC1999/spring-demo.git
cd spring-demo
```

2. Access to Database configuration
```bash
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/demodb?currentSchema=company
    username: db_user
    password: user123

    jpa:
      hibernate:
        ddl-auto: validate
```

3. Compile the project
```bash
mvn clean install
```

4. Run the project
```bash
mvn spring-boot:run
```

---

## Run with Docker
```bash
docker compose up --build
```

The application will be available at:

http://localhost:8080

---

## Usage
### Main endpoints
- **Authorization**
    - Register: POST /auth/register
    - Log in: POST /auth/login

- **Employees**
    - List all employees: GET /employees
    - Obtain a employee:
      - By ID: GET /employees/{id}
      - By email: GET /employees/search?email=example
      - By name: GET /employees/search-by-name?name=example
      - By position: GET /employees/search-by-position?position=DEVELOPER
    - List employee's tasks: /employees/{id}/tasks
    - Assign a project: PUT /employees/{id}/assignProject/{projectId}
    - Unassign a project: PUT /employees/{id}/unassignProject/{projectId}

- **Tasks**
  - List all tasks: GET /tasks (Pageable)
  - Obtain tasks:
      - By ID: GET /tasks/{id}
      - By description: GET /tasks/search-by-description?description="description"
      - By status: GET /tasks/search-by-status?status=status
      - unassigned: GET /tasks/search-unassigned
    - Assign it to an employee: PUT /tasks/{id}/assign/{employeeId}

- **Projects**
  - List all projects: GET /projects
  - Obtain projects:
    - By ID: GET /projects/{id}
    - By name: GET /projects/search?name="name"
  - List project employees: GET /{id}/employees
  - List project tasks: GET /{id}/tasks

## Project architecture

```text
src/main/java/com/example/demoPersonal
│
├── config/         # Global configuration
├── controller/     # REST endpoints
├── service/        # Business rules
├── repository/     # Database access (Spring JPA)
├── entity/         # JPA entities and enums
├── dto/            # Data Transfer Objects
├── mapper/         # Conversión Entity ↔ DTO
├── security/       # Security configuration (Spring Security)
└── exception/      # Customized and centralized exceptions.

```

## Roadmap

### Completed

- Implemented layered architecture (Controller → Service → Repository)  ✅
- Entity modeling with JPA/Hibernate (relationships, lazy loading)  ✅
- DTO mapping using Java Records  ✅
- Global exception handling (GlobalExceptionHandler)  ✅
- Business rules implemented in services  ✅
- Logging using SLF4J  ✅
- JWT authentication & authorization (Spring Security)  ✅
- Password hashing with BCrypt  ✅
- Unit tests for service layer (JUnit + Mockito)  ✅
- API tested manually using Postman  ✅
- Initial documentation and project setup  ✅
- Implement flyway to manage db migrations  ✅
- Implement roles and endpoint control access  ✅
- Create endpoint "self" (Employees with role "User" just can see their tasks, and their project unassigned tasks.)  ✅
- Refactor ID and use UUID instead.  ✅
- Controller layer tests (MockMvc)  ✅
- Document API using OpenAPI/Swagger  ✅
- Containerization with Docker  ✅

### TO-DO

### Considerations
- CI Pipeline (GitHub Actions)
- Metrics and Monitoring
- Deploy in cloud (AWS)

---

## Author
Rubén Agra Casal

Gmail: rubenagra99@gmail.com

LinkedIn: [rubenagradev](https://www.linkedin.com/in/rubenagradev/)


