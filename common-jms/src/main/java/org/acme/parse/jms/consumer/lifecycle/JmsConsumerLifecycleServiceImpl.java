package org.acme.parse.jms.consumer.lifecycle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JmsConsumerLifecycleServiceImpl implements JmsConsumerLifecycleService {

    private final JmsListenerEndpointRegistry listenerEndpointRegistry;
    private final String listenerId;

    public JmsConsumerLifecycleServiceImpl(
            JmsListenerEndpointRegistry listenerEndpointRegistry,
            @Value("${app.jms.listener-id}") String listenerId) {
        this.listenerEndpointRegistry = listenerEndpointRegistry;
        this.listenerId = listenerId;
    }

    @Override
    public JmsConsumerListenerState pause(@Nullable String listenerIdParam) {
        String id = resolveListenerId(listenerIdParam);
        MessageListenerContainer c = requireContainer(id);
        if (c.isRunning()) {
            c.stop();
            log.info("Paused JMS listener id={}", id);
        } else {
            log.info("JMS listener id={} was already paused (not running)", id);
        }
        return new JmsConsumerListenerState(id, c.isRunning());
    }

    @Override
    public JmsConsumerListenerState resume(@Nullable String listenerIdParam) {
        String id = resolveListenerId(listenerIdParam);
        MessageListenerContainer c = requireContainer(id);
        if (!c.isRunning()) {
            c.start();
            log.info("Resumed JMS listener id={}", id);
        } else {
            log.info("JMS listener id={} was already running", id);
        }
        return new JmsConsumerListenerState(id, c.isRunning());
    }

    private String resolveListenerId(@Nullable String listenerIdParam) {
        return StringUtils.hasText(listenerIdParam) ? listenerIdParam.trim() : listenerId;
    }

    private MessageListenerContainer requireContainer(String id) {
        MessageListenerContainer c = listenerEndpointRegistry.getListenerContainer(id);
        if (c == null) {
            throw new JmsListenerNotFoundException(id);
        }
        return c;
    }
}
