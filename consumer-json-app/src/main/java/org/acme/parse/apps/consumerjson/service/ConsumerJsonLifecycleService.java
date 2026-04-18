package org.acme.parse.apps.consumerjson.service;

import org.springframework.lang.Nullable;

import org.acme.parse.jms.consumer.lifecycle.JmsConsumerListenerState;

public interface ConsumerJsonLifecycleService {

    JmsConsumerListenerState pause(@Nullable String listenerIdParam);

    JmsConsumerListenerState resume(@Nullable String listenerIdParam);
}
