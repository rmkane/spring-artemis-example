package org.acme.parse.producer.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import org.acme.parse.common.model.Message;
import org.acme.parse.common.service.MessageService;
import org.acme.parse.producer.service.JmsPublishResult;
import org.acme.parse.producer.service.JmsPublishService;
import org.acme.parse.producer.service.JmsWireFormat;

@Service
@Slf4j
public class JmsPublishServiceImpl implements JmsPublishService {

    private final JmsTemplate jmsTemplate;
    private final MessageService messageService;
    private final String queueName;

    public JmsPublishServiceImpl(
            JmsTemplate jmsTemplate,
            MessageService messageService,
            @Value("${app.jms.queue}") String queueName) {
        this.jmsTemplate = jmsTemplate;
        this.messageService = messageService;
        this.queueName = queueName;
    }

    @SuppressWarnings("null")
    @Override
    public JmsPublishResult publish(Message message, JmsWireFormat wireFormat) {
        String normalized = wireFormat.name();
        String payload =
                switch (wireFormat) {
                    case json -> messageService.serializeToJson(message);
                    case xml -> messageService.serializeToXml(message);
                };

        jmsTemplate.convertAndSend(queueName, payload);

        log.info(
                "Published JMS TextMessage destination={} wireFormat={} payloadChars={}",
                queueName,
                normalized,
                payload.length());

        return new JmsPublishResult(queueName, normalized, payload.length());
    }
}
