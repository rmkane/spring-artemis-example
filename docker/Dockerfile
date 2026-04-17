# syntax=docker/dockerfile:1
#
# Pairing: Dockerfile.runtime = minimal JVM layer (BASE_IMAGE). This file = Maven compile only.
#
# Builds exactly one Spring Boot application (APP_MODULE):
#   1) Install the root aggregator POM into the local repo (mvn -N; no module dirs required).
#   2) Build and install common-jms (the only shared library all apps use).
#   3) Copy and package only APP_MODULE — no other Spring Boot apps are copied or compiled.
#
# Final stage uses BASE_IMAGE (docker/Dockerfile.runtime); only the fat jar is added here.

ARG BASE_IMAGE=parse/jvm-runtime:21

FROM maven:3.9.14-eclipse-temurin-21-alpine AS build
WORKDIR /workspace

ARG APP_MODULE
RUN test -n "$APP_MODULE"

COPY pom.xml .
RUN mvn -N -q install

COPY common-jms/pom.xml common-jms/
RUN mvn -q -f common-jms/pom.xml dependency:go-offline || true

COPY common-jms common-jms
RUN mvn -q -f common-jms/pom.xml install -DskipTests

COPY ${APP_MODULE}/pom.xml ${APP_MODULE}/
RUN mvn -q -f ${APP_MODULE}/pom.xml dependency:go-offline || true

COPY ${APP_MODULE} ${APP_MODULE}/
RUN mvn -q -f ${APP_MODULE}/pom.xml package -DskipTests

FROM ${BASE_IMAGE}

ARG APP_MODULE
COPY --from=build /workspace/${APP_MODULE}/target/${APP_MODULE}-*.jar /app/application.jar
