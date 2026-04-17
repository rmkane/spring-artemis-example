package org.acme.parse.jms.consumer.lifecycle;

/** Thrown when no {@link org.springframework.jms.annotation.JmsListener} is registered for the expected id. */
public class JmsListenerNotFoundException extends RuntimeException {

    public JmsListenerNotFoundException(String listenerId) {
        super("No JMS listener registered with id: " + listenerId);
    }
}
