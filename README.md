# Events Project

Spring Boot application for event management with CI/CD pipeline.

## Technologies

- Spring Boot 2.7.18
- Java 8
- MySQL
- Docker & Docker Compose
- Jenkins CI/CD
- SonarQube
- Nexus
- Prometheus & Grafana

## Build

```bash
mvn clean package
```

## Run

```bash
docker compose up -d
```

## Access

- Application: http://localhost:8089/events
- Swagger UI: http://localhost:8089/events/swagger-ui.html
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/admin)

