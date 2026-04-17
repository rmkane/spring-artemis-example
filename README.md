# Spring Artemis Test

Maven monorepo with **two producer** and **two consumer** Spring Boot apps, a shared **`common-jms`** library (Artemis/JMS **topics** for data, **queues** for dead letters, publish API, consumer lifecycle, **retry**), and **Docker Compose** to run Artemis plus all services.

## Prerequisites

- **Java 21**
- **Maven 3.9+**
- **Docker** (for Artemis and optional full stack)
- **GNU Make** (optional; see `make help`)

## Scripts (`scripts/`)

Small **`curl`** helpers for **`POST /api/jms/consume/pause`** and **`resume`**. Naming: **`{json|xml}-{main|queue|topic|durable}-{pause|resume}.sh`**.

| Prefix | App | Default base URL |
| ------ | --- | ---------------- |
| `json-*` | `consumer-json-app` | `http://localhost:8083` (`CONSUMER_JSON_BASE`) |
| `xml-*` | `consumer-xml-app` | `http://localhost:8084` (`CONSUMER_XML_BASE`) |

| Script stem | JMS listener id / meaning |
| ----------- | ------------------------- |
| `json-main-*` | Main business topic (`app.jms.listener-id`, no query param) |
| `json-queue-*` | Demo point-to-point queue (`demo-pp-queue`) |
| `json-topic-*` | Demo non-durable topic (`demo-pubsub-topic`) |
| `json-durable-*` | Demo durable topic (`demo-durable-topic`) |
| `xml-main-*` | Main business topic for XML consumer |

Examples:

```bash
./scripts/json-queue-pause.sh
CONSUMER_JSON_BASE=http://localhost:9093 ./scripts/json-main-resume.sh
```

## Makefile

From the repository root, `make help` lists common targets: `verify`, `run MODULE=…`, **`run-producer-json`**, **`run-producer-xml`**, **`run-consumer-json`**, **`run-consumer-xml`** (each **`cd`s into that module** and runs **`mvn spring-boot:run`** with hard-coded **`SERVER_PORT`** **8081–8084** and default **`ARTEMIS_BROKER_URL`** `tcp://127.0.0.1:61616`), `base-image`, `artemis`, `compose-up-build`, `images`, and `image MODULE=…`. **`make base-image`** builds **`docker/Dockerfile.runtime`** (default tag **`parse/jvm-runtime:21`**); app images add the jar via **`docker/Dockerfile`**. Override the tag with **`make base-image RUNTIME_IMAGE=…`** and the same value for **`BASE_IMAGE`** in `docker-compose.yml` if you rename it.

## Modules

| Module | Role |
| ------ | ---- |
| `common-jms` | Domain model, serialization/deserialization, JMS publish REST + service, consumer pause/resume REST, `RetryTemplate`, DLQ publisher, topic/queue `JmsTemplate` beans, HTTP startup URL log |
| `producer-json-app` | HTTP → JMS topic `app.messages.json` |
| `producer-xml-app` | HTTP → JMS topic `app.messages.xml` |
| `consumer-json-app` | Subscribes to topic `app.messages.json`; failures → queue `app.messages.json.DLQ`; includes **JMS pattern demos** (queue + topic + durable topic) |
| `consumer-xml-app` | Subscribes to topic `app.messages.xml`; failures → queue `app.messages.xml.DLQ` |

## Send and receive (how it fits together)

Publishing and consuming are **different apps** on purpose:

1. **Send**: `POST` a `Message` body to a **producer** (`/api/jms/publish/messages`). That serializes JSON or XML and **publishes to a JMS topic** (`app.jms.topic`).
2. **Receive**: **Consumers** start their JMS listeners automatically. Watch that consumer’s logs (or attach your own handling). Use **`POST /api/jms/consume/pause`** / **`resume`** to stop or restart receiving for tests (e.g. backup scenarios). Nothing automatically pushes the message back to HTTP.

So you still “send and receive” the same business payload, but the **send** path is producer HTTP and the **receive** path is the consumer process (logs or code you add), not a round-trip in one service.

## Run locally (Artemis in Docker, apps on the host)

Start the broker:

```bash
docker compose up -d artemis
```

Run one app (examples):

```bash
mvn -pl producer-json-app spring-boot:run
```

Override broker URL when not on localhost:

```bash
export ARTEMIS_BROKER_URL=tcp://127.0.0.1:61616
mvn -pl consumer-json-app spring-boot:run
```

**Listeners start with the app** (`spring.jms.listener` uses Boot defaults). **`POST /api/jms/consume/pause`** stops receiving for one listener (default: main `app.jms.listener-id`, or pass **`listenerId`**); **`POST /api/jms/consume/resume`** starts it again. Under the hood these call `MessageListenerContainer.stop()` / `start()`.

Optional query parameter **`listenerId`**: pause or resume a specific `@JmsListener` by id. Omit it for the main business listener (`app.jms.listener-id`). Examples for **consumer-json** on port **8083**:

```bash
curl -sS -X POST 'http://localhost:8083/api/jms/consume/pause?listenerId=demo-pp-queue'
curl -sS -X POST 'http://localhost:8083/api/jms/consume/resume?listenerId=demo-pp-queue'
```

Demo listener ids: **`demo-pp-queue`**, **`demo-pubsub-topic`**, **`demo-durable-topic`**. With a listener **paused**, a **queue** holds messages until you **resume** it; **durable topic** traffic is retained by the broker for that subscription while **non-durable topic** subscribers do not get backlog when offline.

Publish from the **producer** (run `producer-json-app` on another port if both are local, e.g. `-Dserver.port=8081`):

```bash
curl -sS -X POST 'http://localhost:8081/api/jms/publish/messages?format=json' \
  -H 'Content-Type: application/json' \
  -d '{"header":{"version":"1.0","timestamp":"2026-04-16T12:00:00Z","sender":"a","recipient":"b"},"body":{"content":"hello"}}'
```

With **Compose**, use e.g. producer-json on **8081** and consumer-json on **8083** (listeners are already running; publish to **8081**).

### JMS pattern demos (queue vs topic vs durable topic)

**Queues** store messages until a consumer receives them (point-to-point). **Topics** broadcast to subscribers; with a **non-durable** subscription, only consumers that are connected at publish time get a copy. **Durable** topic subscriptions use a stable client id plus subscription name so the broker can keep messages while that subscriber is offline.

`consumer-json-app` registers three demo listeners (see `app.jms.demo.*` and `JmsPatternDemoListeners`); they **start with the app** like the main listener. Use **`pause`** / **`resume`** with **`listenerId`** when you want to stop one listener only (e.g. pause **`demo-pp-queue`** so the demo queue fills for a backup test). From a **producer** (same defaults work without extra YAML):

```bash
curl -sS -X POST http://localhost:8081/api/jms/publish/demo/queue -H 'Content-Type: text/plain' -d 'hello-queue'
curl -sS -X POST http://localhost:8081/api/jms/publish/demo/topic -H 'Content-Type: text/plain' -d 'hello-topic'
curl -sS -X POST http://localhost:8081/api/jms/publish/demo/durable-topic -H 'Content-Type: text/plain' -d 'hello-durable'
```

Watch **consumer-json** logs for `[demo queue]`, `[demo non-durable topic]`, and `[demo durable topic]`. The main business topic listener (`app.messages.json`) is unchanged and can be paused or resumed the same way (`listenerId` omitted).

## Run everything in Compose

App images intentionally use **two Dockerfiles** so concerns stay split:

1. **`docker/Dockerfile.runtime`** — **Runtime only**: distroless Java 21, non-root, no Maven, no Spring Boot SDK, no repo `COPY`. It only knows how to start **`/app/application.jar`** (any fat JAR you place there). Tag default **`parse/jvm-runtime:21`**.
2. **`docker/Dockerfile`** — **Build only**: Maven, this repo’s **`common-jms`**, and one **`APP_MODULE`**. The last stage copies the built jar onto `BASE_IMAGE`.

`make compose-up-build` / `make compose-build` run **`make base-image`** first so `BASE_IMAGE` exists. If you call **`docker compose build`** alone, build the runtime once:  
`docker build -f docker/Dockerfile.runtime -t parse/jvm-runtime:21 .`

```bash
make compose-up-build
```

Each `*-app/` module still has a **`Dockerfile` symlink** to `../docker/Dockerfile` (the *build* recipe). Use **`make image`** / **`make compose-build`** so flags and base image stay consistent.

## Docker layout

| Path | Role |
| ---- | ---- |
| `docker/Dockerfile.runtime` | Minimal JVM userspace (distroless Java 21); not Maven- or framework-specific |
| `docker/Dockerfile` | Maven build for one module + `COPY` jar onto `BASE_IMAGE` (`APP_MODULE`, `BASE_IMAGE` build args) |
| `producer-json-app/Dockerfile`, … | Symlinks to `../docker/Dockerfile` (the **build** recipe; runtime is separate) |

### HTTP ports on the host

| Service | URL |
| ------- | --- |
| producer-json | <http://localhost:8081/swagger-ui/index.html> |
| producer-xml | <http://localhost:8082/swagger-ui/index.html> |
| consumer-json | <http://localhost:8083/swagger-ui/index.html> |
| consumer-xml | <http://localhost:8084/swagger-ui/index.html> |
| Artemis console | <http://localhost:8161> (`artemis` / `artemis`) |

## Retry and failure queues

Consumers wrap handling in a shared **`RetryTemplate`** (`app.jms.retry.*` in each consumer `application.yml`). After retries are exhausted, the payload is sent to the **dead-letter queue** `app.jms.dlq` (still a queue, not a topic) with properties `X-Source-Topic`, `X-Failure-Reason`, and `X-Failure-Type`. The original topic message is then acknowledged (no broker redelivery loop for poison messages).

## Tests

```bash
mvn test
```

## Configuration

Per-app `application.yml` sets `spring.artemis.broker-url` (default `ARTEMIS_BROKER_URL` or `tcp://127.0.0.1:61616`), **`spring.jms.pub-sub-domain: true`** (topics), **`app.jms.topic`**, and for consumers **`app.jms.dlq`** (queue), **`app.jms.listener-id`**, and **`app.jms.retry`**. Optional **`app.jms.demo.*`** names the three pattern-demo destinations on producers and on `consumer-json-app`. **`app.jms.demo.durable-client-id`** must differ from **`spring.jms.client-id`** so the main durable listener and the demo durable listener can run at the same time (JMS: one client id per active connection).

Swagger’s **`listenerId`** dropdown on **`POST /api/jms/consume/pause|resume`** comes from **`JmsListenerIdParameter`** (`common-jms`, `allowableValues`). If you change **`app.jms.listener-id`** away from the defaults `consumer-json` / `consumer-xml`, update that annotation so the UI stays accurate (the API still accepts any registered listener id).

Consumers set **`spring.jms.cache.enabled: false`** so durable topic listeners can call **`Connection.setClientID`** (Spring’s default **`CachingConnectionFactory`** shared connection proxy does not allow that call).
