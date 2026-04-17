package org.acme.parse.common.service.impl;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;

import org.acme.parse.common.model.Message;
import org.acme.parse.common.service.MessageDeserializationService;

@Service
@Slf4j
public class MessageDeserializationServiceImpl implements MessageDeserializationService {

    private final JAXBContext jaxbContext;

    public MessageDeserializationServiceImpl(JAXBContext messageJaxbContext) {
        this.jaxbContext = messageJaxbContext;
    }

    @Override
    public Message deserializeFromXml(String xml) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Message parsed = (Message) unmarshaller.unmarshal(new StringReader(xml));
            log.debug("Parsed message from XML (chars={})", xml.length());
            return parsed;
        } catch (JAXBException e) {
            log.error("Failed to deserialize message from XML (chars={})", xml.length(), e);
            throw new IllegalStateException("Failed to deserialize message from XML", e);
        }
    }

    @Override
    public Message deserializeFromXmlUtf8(byte[] xmlUtf8) {
        return deserializeFromXml(new String(xmlUtf8, StandardCharsets.UTF_8));
    }
}
