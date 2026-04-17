package org.acme.parse.apps.consumerjson.jms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.acme.parse.apps.consumerjson.service.JsonInboundMessageService;
import org.acme.parse.jms.consumer.retry.RetryingJmsPayloadRunner;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsonQueueMessageListener {

    private final RetryingJmsPayloadRunner retryingRunner;
    private final JsonInboundMessageService handler;

    @Value("${app.jms.topic}")
    private String topicName;

    @JmsListener(
            id = "${app.jms.listener-id}",
            destination = "${app.jms.topic}",
            subscription = "${app.jms.durable-subscription}",
            concurrency = "1")
    public void onMessage(String text) {
        retryingRunner.run(topicName, text, handler::handle);
    }
}
