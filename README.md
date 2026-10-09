Flash

A high-concurrency flash sale backend built to handle traffic spikes without overselling

## Stack (currently)
- Java / Spring Boot
- PostgreSQL
- Redis 
- RabbitMQ

Setup

Prerequisites
* Java 27
* Docker/Docker compose
* Gradle

```bash
docker compose up -d
./gradlew bootRun

```

in terminal shell:

Invoke-RestMethod -Method Post -Uri "http://localhost:8443/api/checkout" -ContentType "application/json" -Body '{"userId":"user123", "productId":1, "quantity":1}'

(will improve this later, recovering from framework and abstraction fatigue)