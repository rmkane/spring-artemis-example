package org.acme.parse.apps.producerjson.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import org.acme.parse.apps.producerjson.service.ProducerJsonPublishService;
import org.acme.parse.common.model.Message;
import org.acme.parse.jms.publish.service.JmsPublishResult;
import org.acme.parse.jms.publish.service.JmsWireFormat;
import org.acme.parse.jms.publish.web.MessageApiExamples;

@RestController
@RequestMapping("/api/jms/publish")
public class JmsPublishController {

    private final ProducerJsonPublishService publishService;

    public JmsPublishController(ProducerJsonPublishService publishService) {
        this.publishService = publishService;
    }

    @Operation(summary = "Publish a message to the JMS topic (TextMessage)")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = {
                @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Message.class),
                        examples = {
                            @ExampleObject(
                                    name = "order-notification",
                                    summary = "Order to warehouse",
                                    value = MessageApiExamples.JSON_ORDER),
                            @ExampleObject(
                                    name = "status-ping",
                                    summary = "Ops status message",
                                    value = MessageApiExamples.JSON_STATUS)
                        }),
                @Content(
                        mediaType = MediaType.APPLICATION_XML_VALUE,
                        schema = @Schema(implementation = Message.class),
                        examples = {
                            @ExampleObject(
                                    name = "order-notification",
                                    summary = "Order to warehouse (JAXB XML)",
                                    value = MessageApiExamples.XML_ORDER),
                            @ExampleObject(
                                    name = "status-ping",
                                    summary = "Ops status message (JAXB XML)",
                                    value = MessageApiExamples.XML_STATUS)
                        })
            })
    @PostMapping(
            path = "/messages",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<JmsPublishResult> publish(
            @RequestBody Message message,
            @RequestParam(name = "format", defaultValue = "json") JmsWireFormat format) {

        return ResponseEntity.accepted().body(publishService.publish(message, format));
    }
}
