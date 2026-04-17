package org.acme.parse.consumer.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.acme.parse.consumer.service.JmsConsumerLifecycleService;
import org.acme.parse.consumer.service.JmsConsumerListenerState;

@RestController
@RequestMapping("/api/jms/consume")
@RequiredArgsConstructor
public class JmsConsumerController {

    private final JmsConsumerLifecycleService consumerLifecycleService;

    @Operation(summary = "Start consuming messages from the configured JMS queue")
    @PostMapping("/start")
    public ResponseEntity<JmsConsumerListenerState> start() {
        return ResponseEntity.ok(consumerLifecycleService.start());
    }

    @Operation(summary = "Stop consuming messages from the configured JMS queue")
    @PostMapping("/stop")
    public ResponseEntity<JmsConsumerListenerState> stop() {
        return ResponseEntity.ok(consumerLifecycleService.stop());
    }
}
