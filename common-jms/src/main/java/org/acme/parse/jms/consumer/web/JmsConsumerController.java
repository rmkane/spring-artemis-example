package org.acme.parse.jms.consumer.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.acme.parse.jms.consumer.lifecycle.JmsConsumerLifecycleService;
import org.acme.parse.jms.consumer.lifecycle.JmsConsumerListenerState;

@RestController
@RequestMapping("/api/jms/consume")
@RequiredArgsConstructor
public class JmsConsumerController {

    private final JmsConsumerLifecycleService consumerLifecycleService;

    @Operation(
            summary = "Pause a JMS listener (stop receiving)",
            description =
                    "Omit `listenerId` to pause the main business listener (`app.jms.listener-id`). "
                            + "Maps to `MessageListenerContainer.stop()`.")
    @PostMapping("/pause")
    public ResponseEntity<JmsConsumerListenerState> pause(
            @JmsListenerIdParameter @RequestParam(name = "listenerId", required = false) String listenerId) {
        return ResponseEntity.ok(consumerLifecycleService.pause(listenerId));
    }

    @Operation(
            summary = "Resume a JMS listener (start receiving)",
            description =
                    "Omit `listenerId` to resume the main business listener (`app.jms.listener-id`). "
                            + "Maps to `MessageListenerContainer.start()`.")
    @PostMapping("/resume")
    public ResponseEntity<JmsConsumerListenerState> resume(
            @JmsListenerIdParameter @RequestParam(name = "listenerId", required = false) String listenerId) {
        return ResponseEntity.ok(consumerLifecycleService.resume(listenerId));
    }
}
