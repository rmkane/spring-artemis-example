package org.acme.parse.jms.consumer.lifecycle;

import org.springframework.lang.Nullable;

public interface JmsConsumerLifecycleService {

    /**
     * Pauses a {@link org.springframework.jms.annotation.JmsListener} container (stops receiving; underlying
     * {@link org.springframework.jms.listener.MessageListenerContainer#stop()}).
     *
     * @param listenerId explicit Spring JMS listener id; if {@code null} or blank, uses {@code app.jms.listener-id}
     * @throws JmsListenerNotFoundException if the listener id is not registered
     */
    JmsConsumerListenerState pause(@Nullable String listenerId);

    /**
     * Resumes a listener container ({@link org.springframework.jms.listener.MessageListenerContainer#start()}).
     *
     * @param listenerId explicit listener id, or {@code null} / blank for {@code app.jms.listener-id}
     * @throws JmsListenerNotFoundException if the listener id is not registered
     */
    JmsConsumerListenerState resume(@Nullable String listenerId);
}
