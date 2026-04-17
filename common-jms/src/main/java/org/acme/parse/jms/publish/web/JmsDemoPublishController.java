package org.acme.parse.jms.publish.web;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

import lombok.extern.slf4j.Slf4j;

import org.acme.parse.jms.publish.service.JmsDemoPublishResult;

/** Dedicated endpoints to publish plain text to each JMS pattern demo destination (see README). */
@RestController
@RequestMapping("/api/jms/publish/demo")
@Slf4j
public class JmsDemoPublishController {

    private final JmsTemplate topicJmsTemplate;
    private final JmsTemplate queueJmsTemplate;
    private final String demoQueue;
    private final String demoTopic;
    private final String demoDurableTopic;

    public JmsDemoPublishController(
            JmsTemplate topicJmsTemplate,
            @Qualifier("queueJmsTemplate") JmsTemplate queueJmsTemplate,
            @Value("${app.jms.demo.queue:app.demo.pp.queue}") String demoQueue,
            @Value("${app.jms.demo.topic:app.demo.pubsub.topic}") String demoTopic,
            @Value("${app.jms.demo.durable-topic:app.demo.durable.topic}") String demoDurableTopic) {
        this.topicJmsTemplate = topicJmsTemplate;
        this.queueJmsTemplate = queueJmsTemplate;
        this.demoQueue = demoQueue;
        this.demoTopic = demoTopic;
        this.demoDurableTopic = demoDurableTopic;
    }

    @Operation(summary = "Publish text to the demo point-to-point queue (messages wait for a consumer)")
    @PostMapping(path = "/queue", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<JmsDemoPublishResult> publishQueue(@RequestBody String body) {
        queueJmsTemplate.convertAndSend(demoQueue, body);
        log.info("Published demo queue destination={} chars={}", demoQueue, body.length());
        return ResponseEntity.accepted().body(new JmsDemoPublishResult("queue", demoQueue, body.length()));
    }

    @Operation(summary = "Publish text to the demo non-durable topic (only active subscribers receive it)")
    @PostMapping(path = "/topic", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<JmsDemoPublishResult> publishTopic(@RequestBody String body) {
        topicJmsTemplate.convertAndSend(demoTopic, body);
        log.info("Published demo non-durable topic destination={} chars={}", demoTopic, body.length());
        return ResponseEntity.accepted().body(new JmsDemoPublishResult("topic", demoTopic, body.length()));
    }

    @Operation(summary = "Publish text to the demo durable topic (broker retains for the named subscription)")
    @PostMapping(path = "/durable-topic", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<JmsDemoPublishResult> publishDurableTopic(@RequestBody String body) {
        topicJmsTemplate.convertAndSend(demoDurableTopic, body);
        log.info("Published demo durable topic destination={} chars={}", demoDurableTopic, body.length());
        return ResponseEntity.accepted().body(new JmsDemoPublishResult("durable-topic", demoDurableTopic, body.length()));
    }
}
