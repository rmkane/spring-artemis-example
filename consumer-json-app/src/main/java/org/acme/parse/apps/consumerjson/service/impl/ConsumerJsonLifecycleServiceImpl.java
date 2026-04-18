package org.acme.parse.apps.consumerjson.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.acme.parse.apps.consumerjson.service.ConsumerJsonLifecycleService;
import org.acme.parse.jms.consumer.lifecycle.JmsConsumerListenerState;
import org.acme.parse.jms.consumer.lifecycle.JmsListenerNotFoundException;

@Service
public class ConsumerJsonLifecycleServiceImpl implements ConsumerJsonLifecycleService {

    private static final Logger log = LoggerFactory.getLogger(ConsumerJsonLifecycleServiceImpl.class);

    private final JmsListenerEndpointRegistry listenerEndpointRegistry;
    private final String listenerId;

    public ConsumerJsonLifecycleServiceImpl(
            JmsListenerEndpointRegistry listenerEndpointRegistry,
            @Value("${app.jms.listener-id}") String listenerId) {
        this.listenerEndpointRegistry = listenerEndpointRegistry;
        this.listenerId = listenerId;
    }

    @Override
    public JmsConsumerListenerState pause(@Nullable String listenerIdParam) {
        String id = resolveListenerId(listenerIdParam);
        MessageListenerContainer container = requireContainer(id);
        if (container.isRunning()) {
            container.stop();
            log.info("Paused JMS listener id={}", id);
        } else {
            log.info("JMS listener id={} was already paused (not running)", id);
        }
        return new JmsConsumerListenerState(id, container.isRunning());
    }

    @Override
    public JmsConsumerListenerState resume(@Nullable String listenerIdParam) {
        String id = resolveListenerId(listenerIdParam);
        MessageListenerContainer container = requireContainer(id);
        if (!container.isRunning()) {
            container.start();
            log.info("Resumed JMS listener id={}", id);
        } else {
            log.info("JMS listener id={} was already running", id);
        }
        return new JmsConsumerListenerState(id, container.isRunning());
    }

    private String resolveListenerId(@Nullable String listenerIdParam) {
        return StringUtils.hasText(listenerIdParam) ? listenerIdParam.trim() : listenerId;
    }

    private MessageListenerContainer requireContainer(String id) {
        MessageListenerContainer container = listenerEndpointRegistry.getListenerContainer(id);
        if (container == null) {
            throw new JmsListenerNotFoundException(id);
        }
        return container;
    }
}
