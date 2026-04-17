package org.acme.parse.apps.consumerjson.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Minimal listeners illustrating queue vs topic vs durable topic. See {@code application.yml}
 * {@code app.jms.demo.*} and {@link org.acme.parse.apps.consumerjson.config.JmsPatternListenerContainerFactoriesConfiguration}.
 */
@Component
@Slf4j
public class JmsPatternDemoListeners {

    @JmsListener(
            id = "demo-pp-queue",
            destination = "${app.jms.demo.queue}",
            containerFactory = "queueJmsListenerContainerFactory",
            concurrency = "1")
    public void onQueue(String text) {
        log.info("[demo queue] {}", text);
    }

    @JmsListener(
            id = "demo-pubsub-topic",
            destination = "${app.jms.demo.topic}",
            containerFactory = "topicNonDurableJmsListenerContainerFactory",
            concurrency = "1")
    public void onNonDurableTopic(String text) {
        log.info("[demo non-durable topic] {}", text);
    }

    @JmsListener(
            id = "demo-durable-topic",
            destination = "${app.jms.demo.durable-topic}",
            subscription = "${app.jms.demo.durable-subscription}",
            containerFactory = "durableTopicDemoJmsListenerContainerFactory",
            concurrency = "1")
    public void onDurableTopic(String text) {
        log.info("[demo durable topic] {}", text);
    }
}
