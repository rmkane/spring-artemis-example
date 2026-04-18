package org.acme.parse.apps.consumerxml.service;

import org.springframework.lang.Nullable;

import org.acme.parse.jms.consumer.lifecycle.JmsConsumerListenerState;

public interface ConsumerXmlLifecycleService {

    JmsConsumerListenerState pause(@Nullable String listenerIdParam);

    JmsConsumerListenerState resume(@Nullable String listenerIdParam);
}
