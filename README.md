# Spring Artemis Test

Small Spring Boot app that **publishes** JSON/XML messages to an Apache Artemis queue and **consumes** them with an on-demand JMS listener. Uses **Jakarta XML Binding** for XML, **Spring JMS**, and **Springdoc OpenAPI** for Swagger UI.

## Prerequisites

- **Java 21**
- **Maven 3.9+**
- **Docker** (for the Artemis broker)

## 1. Start Artemis

```bash
docker compose up -d
```

- **JMS:** `tcp://127.0.0.1:61616` (matches `application.yml`)
- **Console:** <http://localhost:8161> — user `artemis`, password `artemis`

## 2. Run the application

```bash
mvn spring-boot:run
```

Default HTTP port: **8080**.

The JMS listener does **not** start until you call the consume **start** endpoint (`spring.jms.listener.auto-startup: false`).

## 3. Try the flow

**Start the consumer** (begins receiving from queue `app.messages`):

```bash
curl -sS -X POST http://localhost:8080/api/jms/consume/start
```

**Publish** a JSON body (`format` = `json` or `xml` for the wire encoding on the JMS text message):

```bash
curl -sS -X POST 'http://localhost:8080/api/jms/publish/messages?format=json' \
  -H 'Content-Type: application/json' \
  -d '{"header":{"version":"1.0","timestamp":"2026-04-16T12:00:00Z","sender":"a","recipient":"b"},"body":{"content":"hello"}}'
```

**Stop the consumer:**

```bash
curl -sS -X POST http://localhost:8080/api/jms/consume/stop
```

Watch application logs for publish and consumer handler lines.

## 4. Swagger UI

Open **<http://localhost:8080/swagger-ui/index.html>** to try publish/consume APIs with sample payloads.

## 5. Tests

```bash
mvn test
```

## Project layout (packages)

| Package | Role |
| ------- | ---- |
| `org.acme.parse.common` | Shared model, `MessageService` (JSON/JAXB), global REST exception handling |
| `org.acme.parse.producer` | REST publish API and JMS publish service |
| `org.acme.parse.consumer` | JMS listener, dispatch to JSON/XML handlers, consume lifecycle REST API |

## Configuration

Key settings live in **`src/main/resources/application.yml`**: Artemis broker URL, credentials, queue name `app.jms.queue`, and JMS listener auto-startup.
