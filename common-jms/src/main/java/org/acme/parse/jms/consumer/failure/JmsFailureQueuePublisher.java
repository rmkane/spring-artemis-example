package org.acme.parse.jms.consumer.failure;

/**
 * Sends the original TextMessage body to the configured DLQ with broker-friendly headers after
 * application-side retries are exhausted.
 */
public interface JmsFailureQueuePublisher {

    void publish(String sourceTopic, String payload, Throwable error);
}
