package org.acme.parse.producer.service;

import org.acme.parse.common.model.Message;

/** Outcome of publishing a domain {@link Message} to the JMS queue. */
public record JmsPublishResult(String destination, String format, int payloadChars) {}
