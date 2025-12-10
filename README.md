# Task Manager API 
Task Manager API to manage employees, tasks and projects.

---

## Description
A clean and well-structured API created using Spring Boot in a layered architecture. 
This is my first project using Spring, developed with the purpose of learn backend professional concepts.

---

### What I learned

- **Spring Boot** core concepts (IoC, DI, component scanning)

- **REST API** design and best practices

- **Layered architecture** (Controller → Service → Repository)

- **JPA/Hibernate** (entity mapping, relationships, lazy and eager loading)

- DTO usage and mapping (using Java Records)

- Centralized exception handling (**GlobalExceptionHandler**)

- **JWT** authentication with Spring Security

- Logging strategy with SLF4J

- Writing unit tests with JUnit + Mockito

- Testing workflows using Postman

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
        ddl-auto: update
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

## Usage
### Main endpoints
- **Authorization**
    - Generate JWT: POST /auth/login

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

## Project architecture (Layered)

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

- Implemented layered architecture (Controller → Service → Repository)
- Entity modeling with JPA/Hibernate (relationships, lazy loading)
- DTO mapping using Java Records
- Global exception handling (GlobalExceptionHandler)
- Business rules implemented in services
- Logging using SLF4J
- JWT authentication & authorization (Spring Security)
- Password hashing with BCrypt
- Unit tests for service layer (JUnit + Mockito)
- API tested manually using Postman
- Initial documentation and project setup

### TO-DO
- Controller layer tests (MockMvc)
- Document API using OpenAPI/Swagger
- Containerization with Docker
- Improve README with new funcionalities

### Considerations
- Register endpoint
- New employee roles
- CI Pipeline (GitHub Actions)
- Database migrations (with Flyway)
- Metrics and Monitoring
- Deploy in cloud (AWS)

---

## Author
Rubén Agra Casal

Gmail: rubenagra99@gmail.com

LinkedIn: [rubenagradev](https://www.linkedin.com/in/rubenagradev/)


