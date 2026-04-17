package org.acme.parse.consumer.service;

public record JmsConsumerListenerState(String listenerId, boolean running) {}
