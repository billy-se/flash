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

```bash
jmeter -n -t "Thread Group.jmx" -l results.jtl
```
---
Performance

| Metric | Value |
| :--- | :--- |
| **Concurrency** | 5,000 threads |
| **Error Rate** | 12.62% |
| **Mean Latency** | 7,648 ms |
| **Throughput** | 386.76 req/s |

### Notes
* Redis handles zero stock atomically
* PostgreSQL successfully records valid orders asynchronously via RabbitMQ
* Errors and latency spike due to connection pool saturation or hardware limitation at 5,000 threads 