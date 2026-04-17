package org.acme.parse.jms.publish.service;

import org.acme.parse.common.model.Message;

/** Outcome of publishing a domain {@link Message} to the JMS topic (destination name is the topic). */
public record JmsPublishResult(String destination, String format, int payloadChars) {}

