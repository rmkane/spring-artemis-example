package org.acme.parse.consumer.service;

import org.acme.parse.consumer.jms.JmsListenerNotFoundException;

public interface JmsConsumerLifecycleService {

    /**
     * Starts the queue listener container.
     *
     * @throws JmsListenerNotFoundException if the listener id is not registered
     */
    JmsConsumerListenerState start();

    /**
     * Stops the queue listener container.
     *
     * @throws JmsListenerNotFoundException if the listener id is not registered
     */
    JmsConsumerListenerState stop();
}
