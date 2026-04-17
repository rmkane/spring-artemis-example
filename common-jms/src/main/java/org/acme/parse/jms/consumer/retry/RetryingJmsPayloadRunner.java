package org.acme.parse.jms.consumer.retry;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.acme.parse.jms.consumer.failure.JmsFailureQueuePublisher;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetryingJmsPayloadRunner {

    private final RetryTemplate jmsProcessingRetryTemplate;
    private final JmsFailureQueuePublisher failureQueuePublisher;

    public void run(String sourceTopic, String payload, JmsPayloadHandler handler) {
        if (payload == null) {
            log.warn("Ignoring null topic payload (topic={})", sourceTopic);
            return;
        }
        try {
            RetryCallback<Void, Exception> callback =
                    context -> {
                        handler.handle(payload);
                        return null;
                    };
            jmsProcessingRetryTemplate.execute(callback);
        } catch (Exception ex) {
            log.error(
                    "JMS handler failed after retries topic={} payloadChars={}",
                    sourceTopic,
                    payload.length(),
                    ex);
            failureQueuePublisher.publish(sourceTopic, payload, ex);
        }
    }
}
