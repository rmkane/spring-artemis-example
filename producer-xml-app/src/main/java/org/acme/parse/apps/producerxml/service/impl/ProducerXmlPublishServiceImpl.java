package org.acme.parse.apps.producerxml.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.acme.parse.apps.producerxml.service.ProducerXmlPublishService;
import org.acme.parse.common.model.Message;
import org.acme.parse.common.service.MessageSerializationService;
import org.acme.parse.jms.publish.service.JmsPublishResult;
import org.acme.parse.jms.publish.service.JmsWireFormat;

@Service
public class ProducerXmlPublishServiceImpl implements ProducerXmlPublishService {

    private static final Logger log = LoggerFactory.getLogger(ProducerXmlPublishServiceImpl.class);

    private final JmsTemplate jmsTemplate;
    private final MessageSerializationService messageSerializationService;
    private final String topicName;

    public ProducerXmlPublishServiceImpl(
            JmsTemplate jmsTemplate,
            MessageSerializationService messageSerializationService,
            @Value("${app.jms.topic}") String topicName) {
        this.jmsTemplate = jmsTemplate;
        this.messageSerializationService = messageSerializationService;
        this.topicName = topicName;
    }

    @SuppressWarnings("null")
    @Override
    public JmsPublishResult publish(Message message, JmsWireFormat wireFormat) {
        String normalized = wireFormat.name();
        String payload =
                switch (wireFormat) {
                    case json -> messageSerializationService.serializeToJson(message);
                    case xml -> messageSerializationService.serializeToXml(message);
                };

        jmsTemplate.convertAndSend(topicName, payload);

        log.info(
                "Published JMS TextMessage topic={} wireFormat={} payloadChars={}",
                topicName,
                normalized,
                payload.length());

        return new JmsPublishResult(topicName, normalized, payload.length());
    }
}
