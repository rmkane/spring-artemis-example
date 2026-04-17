package org.acme.parse.jms.consumer.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Documents {@code listenerId} for OpenAPI/Swagger (dropdown of known ids). Must stay aligned with
 * {@code app.jms.listener-id} defaults on consumer apps and {@code JmsPatternDemoListeners} ids.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        name = "listenerId",
        required = false,
        description =
                "Omit this parameter to pause/resume the main business listener (app.jms.listener-id). "
                        + "Demo ids exist only on consumer-json-app. Unknown ids return 404.",
        schema =
                @Schema(
                        type = "string",
                        allowableValues = {
                            "consumer-json",
                            "consumer-xml",
                            "demo-pp-queue",
                            "demo-pubsub-topic",
                            "demo-durable-topic"
                        }))
public @interface JmsListenerIdParameter {}
