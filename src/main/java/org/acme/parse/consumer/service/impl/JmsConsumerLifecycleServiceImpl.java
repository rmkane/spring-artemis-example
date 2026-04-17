package org.acme.parse.consumer.service.impl;

import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.acme.parse.consumer.jms.JmsListenerIds;
import org.acme.parse.consumer.jms.JmsListenerNotFoundException;
import org.acme.parse.consumer.service.JmsConsumerListenerState;
import org.acme.parse.consumer.service.JmsConsumerLifecycleService;

@Service
@RequiredArgsConstructor
@Slf4j
public class JmsConsumerLifecycleServiceImpl implements JmsConsumerLifecycleService {

    private final JmsListenerEndpointRegistry listenerEndpointRegistry;

    @Override
    public JmsConsumerListenerState start() {
        MessageListenerContainer c = requireContainer();
        if (!c.isRunning()) {
            c.start();
            log.info("Started JMS listener id={}", JmsListenerIds.MESSAGE_QUEUE);
        } else {
            log.info("JMS listener id={} was already running", JmsListenerIds.MESSAGE_QUEUE);
        }
        return new JmsConsumerListenerState(JmsListenerIds.MESSAGE_QUEUE, c.isRunning());
    }

    @Override
    public JmsConsumerListenerState stop() {
        MessageListenerContainer c = requireContainer();
        if (c.isRunning()) {
            c.stop();
            log.info("Stopped JMS listener id={}", JmsListenerIds.MESSAGE_QUEUE);
        } else {
            log.info("JMS listener id={} was already stopped", JmsListenerIds.MESSAGE_QUEUE);
        }
        return new JmsConsumerListenerState(JmsListenerIds.MESSAGE_QUEUE, c.isRunning());
    }

    private MessageListenerContainer requireContainer() {
        MessageListenerContainer c =
                listenerEndpointRegistry.getListenerContainer(JmsListenerIds.MESSAGE_QUEUE);
        if (c == null) {
            throw new JmsListenerNotFoundException(JmsListenerIds.MESSAGE_QUEUE);
        }
        return c;
    }
}
