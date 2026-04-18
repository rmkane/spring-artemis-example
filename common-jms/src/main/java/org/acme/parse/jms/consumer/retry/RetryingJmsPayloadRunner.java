package org.acme.parse.jms.consumer.retry;

public interface RetryingJmsPayloadRunner {

    void run(String sourceTopic, String payload, JmsPayloadHandler handler);
}
