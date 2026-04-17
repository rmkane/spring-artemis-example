package org.acme.parse.consumer.jms;

/** Must match {@code @JmsListener#id} for lifecycle control via {@link org.springframework.jms.config.JmsListenerEndpointRegistry}. */
public final class JmsListenerIds {

    public static final String MESSAGE_QUEUE = "messageQueueListener";

    private JmsListenerIds() {}
}
