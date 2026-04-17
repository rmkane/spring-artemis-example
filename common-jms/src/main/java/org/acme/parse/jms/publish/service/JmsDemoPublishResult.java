package org.acme.parse.jms.publish.service;

/** Result of publishing to one of the JMS pattern demo destinations (queue, topic, durable topic). */
public record JmsDemoPublishResult(String mode, String destination, int payloadChars) {}
