package org.acme.parse.jms.publish.service;

import org.acme.parse.common.model.Message;

public interface JmsPublishService {

    /**
     * Serializes {@code message} and publishes it as a JMS {@code TextMessage} to the configured topic.
     */
    JmsPublishResult publish(Message message, JmsWireFormat wireFormat);
}
