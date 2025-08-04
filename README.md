# Adaptive Quiz Engine

A backend system for adaptive quiz testing, built with Spring Boot and PostgreSQL. Supports dynamic question difficulty, session tracking, analytics, and role-based access.

## Features

- JWT-based authentication
- Adaptive question selection based on answer history
- Role-based access (USER / ADMIN)
- Session lifecycle: start → get next question → submit answer → finish
- Analytics per topic, difficulty, and session
- Swagger UI for easy API exploration
- Dockerized with PostgreSQL container
- Admin auto-creation on first Docker run

## Getting Started

### Clone the repository

```
git clone https://github.com/11WhyNot11/adaptive-quiz-engine.git
cd adaptive-quiz-engine
```

### Run with Docker

Make sure Docker and Docker Compose are installed.

Start the application:

```
docker-compose up --build
```

This will:

- Start a PostgreSQL container
- Build and run the Spring Boot application
- Automatically create an admin user on first launch

### Default admin credentials

- Email: `admin@example.com`
- Password: `123456`

### Access Swagger UI

Visit [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) to explore and test the API.

## Configuration Profiles

- `default` — used for local development
- `docker` — used when running via Docker; triggers admin user creation

