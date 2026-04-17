package org.acme.parse.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import org.acme.parse.service.MessageService;
import org.acme.parse.model.Message;

@RestController
@RequestMapping("/api/jms")
public class JmsMessageController {

    private static final Logger log = LoggerFactory.getLogger(JmsMessageController.class);

    private final JmsTemplate jmsTemplate;
    private final MessageService messageService;
    private final String queueName;

    public JmsMessageController(
            JmsTemplate jmsTemplate,
            MessageService messageService,
            @Value("${app.jms.queue}") String queueName) {
        this.jmsTemplate = jmsTemplate;
        this.messageService = messageService;
        this.queueName = queueName;
    }

    /**
     * Serializes the body and sends it as a JMS {@code TextMessage} to {@code app.jms.queue}.
     */
    @SuppressWarnings("null")
    @Operation(summary = "Send a message to the JMS queue")
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
    public ResponseEntity<JmsSendResponse> send(
            @RequestBody Message message,
            @RequestParam(name = "format", defaultValue = "json") JmsWireFormat format) {

        String normalized = format.name();
        String payload =
                switch (format) {
                    case json -> messageService.serializeToJson(message);
                    case xml -> messageService.serializeToXml(message);
                };

        jmsTemplate.convertAndSend(queueName, payload);

        log.info(
                "Published JMS TextMessage destination={} wireFormat={} payloadChars={}",
                queueName,
                normalized,
                payload.length());

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new JmsSendResponse(queueName, normalized, payload.length()));
    }

    public record JmsSendResponse(String destination, String format, int payloadChars) {}
}
