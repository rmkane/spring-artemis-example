# Repository root — docker/Dockerfile.runtime = JVM-only layer; docker/Dockerfile = Maven build + jar.
COMPOSE ?= docker compose
MVN ?= mvn
DOCKERFILE := docker/Dockerfile
DOCKERFILE_RUNTIME := docker/Dockerfile.runtime
IMAGE_PREFIX ?= parse
RUNTIME_IMAGE ?= parse/jvm-runtime:21

BOOT_MODULES := producer-json-app producer-xml-app consumer-json-app consumer-xml-app

# Used by run-* targets (override: make run-producer-json ARTEMIS_BROKER_URL=tcp://other:61616)
ARTEMIS_BROKER_URL ?= tcp://127.0.0.1:61616

.DEFAULT_GOAL := help

.PHONY: help
help:
	@echo "Maven"
	@echo "  make verify              Full reactor: tests + package"
	@echo "  make run MODULE=<id>     mvn -pl MODULE spring-boot:run (optional: SERVER_PORT=... ARTEMIS_BROKER_URL=...)"
	@echo "  Example: SERVER_PORT=9090 make run MODULE=consumer-json-app"
	@echo ""
	@echo "Run Spring Boot locally (Artemis: make artemis first). Each target cd's into the module and runs mvn:"
	@echo "  make run-producer-json    cd producer-json-app && SERVER_PORT=8081 mvn spring-boot:run"
	@echo "  make run-producer-xml     cd producer-xml-app  && SERVER_PORT=8082 mvn spring-boot:run"
	@echo "  make run-consumer-json    cd consumer-json-app && SERVER_PORT=8083 mvn spring-boot:run"
	@echo "  make run-consumer-xml     cd consumer-xml-app  && SERVER_PORT=8084 mvn spring-boot:run"
	@echo "  Broker env: ARTEMIS_BROKER_URL=$(ARTEMIS_BROKER_URL) (override on make command line if needed)"
	@echo ""
	@echo "Shell helpers (repo root ./scripts/): pause/resume JMS listeners via curl — see README \"Scripts\""
	@echo ""
	@echo "Docker (context always repo root)"
	@echo "  $(DOCKERFILE_RUNTIME) — distroless JVM 21 only (no Maven/frameworks); tag: $(RUNTIME_IMAGE)"
	@echo "  $(DOCKERFILE)         — Maven build for one module + COPY jar onto BASE_IMAGE"
	@echo "  Each *-app/Dockerfile -> ../docker/Dockerfile (symlink) for IDE tree."
	@echo "  make base-image        Build $(RUNTIME_IMAGE) from $(DOCKERFILE_RUNTIME) (required before app image builds)"
	@echo "  make compose-build     base-image, then docker compose build"
	@echo "  make compose-up          docker compose up -d"
	@echo "  make compose-up-build    base-image, then docker compose up -d --build"
	@echo "  make compose-down        docker compose down"
	@echo "  make compose-logs        docker compose logs -f"
	@echo "  make artemis             Start only the Artemis broker"
	@echo "  make images              Build all four app images ($(IMAGE_PREFIX)/<module>:local)"
	@echo "  make image MODULE=<id>   Build one image, e.g. MODULE=producer-json-app"
	@echo ""
	@echo "Boot modules: $(BOOT_MODULES)"

.PHONY: verify
verify:
	$(MVN) verify

.PHONY: run
run:
	@test -n "$(MODULE)" || (echo 'Set MODULE, e.g. make run MODULE=producer-json-app' >&2; exit 1)
	$(MVN) -pl $(MODULE) spring-boot:run

.PHONY: run-producer-json run-producer-xml run-consumer-json run-consumer-xml
run-producer-json:
	cd producer-json-app && SERVER_PORT=8081 ARTEMIS_BROKER_URL=$(ARTEMIS_BROKER_URL) $(MVN) spring-boot:run

run-producer-xml:
	cd producer-xml-app && SERVER_PORT=8082 ARTEMIS_BROKER_URL=$(ARTEMIS_BROKER_URL) $(MVN) spring-boot:run

run-consumer-json:
	cd consumer-json-app && SERVER_PORT=8083 ARTEMIS_BROKER_URL=$(ARTEMIS_BROKER_URL) $(MVN) spring-boot:run

run-consumer-xml:
	cd consumer-xml-app && SERVER_PORT=8084 ARTEMIS_BROKER_URL=$(ARTEMIS_BROKER_URL) $(MVN) spring-boot:run

.PHONY: base-image
base-image:
	docker build -f $(DOCKERFILE_RUNTIME) -t $(RUNTIME_IMAGE) .

.PHONY: compose-build compose-up compose-down compose-logs compose-up-build
compose-build: base-image
	$(COMPOSE) build

compose-up:
	$(COMPOSE) up -d

compose-up-build: base-image
	$(COMPOSE) up -d --build

compose-down:
	$(COMPOSE) down

compose-logs:
	$(COMPOSE) logs -f

.PHONY: artemis
artemis:
	$(COMPOSE) up -d artemis

.PHONY: images
images: base-image
	@for m in $(BOOT_MODULES); do \
		echo "==> docker build APP_MODULE=$$m"; \
		docker build -f $(DOCKERFILE) \
			--build-arg BASE_IMAGE=$(RUNTIME_IMAGE) \
			--build-arg APP_MODULE=$$m \
			-t $(IMAGE_PREFIX)/$$m:local .; \
	done

.PHONY: image
image: base-image
	@test -n "$(MODULE)" || (echo 'Set MODULE, e.g. make image MODULE=consumer-xml-app' >&2; exit 1)
	docker build -f $(DOCKERFILE) \
		--build-arg BASE_IMAGE=$(RUNTIME_IMAGE) \
		--build-arg APP_MODULE=$(MODULE) \
		-t $(IMAGE_PREFIX)/$(MODULE):local .
