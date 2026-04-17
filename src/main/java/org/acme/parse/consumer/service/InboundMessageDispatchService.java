package org.acme.parse.consumer.service;

/**
 * Routes queue text payloads to JSON or XML handlers based on a simple structural probe
 * (JMS {@code TextMessage} has no HTTP Content-Type).
 */
public interface InboundMessageDispatchService {

    void dispatch(String payload);
}
