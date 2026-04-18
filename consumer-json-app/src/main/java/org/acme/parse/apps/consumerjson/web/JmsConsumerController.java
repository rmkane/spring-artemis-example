package org.acme.parse.apps.consumerjson.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

import org.acme.parse.apps.consumerjson.service.ConsumerJsonLifecycleService;
import org.acme.parse.jms.consumer.lifecycle.JmsConsumerListenerState;
import org.acme.parse.jms.consumer.web.JmsListenerIdParameter;

@RestController
@RequestMapping("/api/jms/consume")
public class JmsConsumerController {

    private final ConsumerJsonLifecycleService consumerLifecycleService;

    public JmsConsumerController(ConsumerJsonLifecycleService consumerLifecycleService) {
        this.consumerLifecycleService = consumerLifecycleService;
    }

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
