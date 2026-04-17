package org.acme.parse.jms.consumer.lifecycle;

public record JmsConsumerListenerState(String listenerId, boolean running) {}
