package org.acme.parse.jms.consumer.retry;

@FunctionalInterface
public interface JmsPayloadHandler {

    void handle(String payload) throws Exception;
}
