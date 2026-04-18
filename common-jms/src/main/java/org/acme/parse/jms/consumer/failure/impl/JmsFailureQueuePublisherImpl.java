package org.acme.parse.jms.consumer.failure.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.jms.JMSException;
import jakarta.jms.Message;

import lombok.extern.slf4j.Slf4j;

import org.acme.parse.jms.consumer.failure.JmsFailureQueuePublisher;

@Service
@Slf4j
public class JmsFailureQueuePublisherImpl implements JmsFailureQueuePublisher {

    private final JmsTemplate jmsTemplate;
    private final String dlqName;

    public JmsFailureQueuePublisherImpl(
            @Qualifier("queueJmsTemplate") JmsTemplate queueJmsTemplate,
            @Value("${app.jms.dlq}") String dlqName) {
        this.jmsTemplate = queueJmsTemplate;
        this.dlqName = dlqName;
    }

    @Override
    public void publish(String sourceTopic, String payload, Throwable error) {
        jmsTemplate.convertAndSend(dlqName, payload, message -> enrichFailureMessage(message, sourceTopic, error));
        log.warn(
                "Sent message to failure queue dlq={} sourceTopic={} payloadChars={}",
                dlqName,
                sourceTopic,
                payload != null ? payload.length() : 0,
                error);
    }

    private static Message enrichFailureMessage(Message message, String sourceTopic, Throwable error)
            throws JMSException {
        message.setStringProperty("X-Source-Topic", StringUtils.truncate(sourceTopic, 255));
        String reason = error.getMessage() != null ? error.getMessage() : error.getClass().getName();
        message.setStringProperty("X-Failure-Reason", StringUtils.truncate(reason, 255));
        message.setStringProperty("X-Failure-Type", error.getClass().getName());
        return message;
    }
}
