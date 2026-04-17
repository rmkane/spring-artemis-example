package org.acme.parse.common.service.impl;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;

import org.acme.parse.common.model.Message;
import org.acme.parse.common.service.MessageSerializationService;

@Service
@Slf4j
public class MessageSerializationServiceImpl implements MessageSerializationService {

    private final ObjectMapper objectMapper;
    private final JAXBContext jaxbContext;

    public MessageSerializationServiceImpl(ObjectMapper objectMapper, JAXBContext messageJaxbContext) {
        this.objectMapper = objectMapper;
        this.jaxbContext = messageJaxbContext;
    }

    @Override
    public String serializeToJson(Message message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            log.debug("Serialized message to JSON (chars={})", json.length());
            return json;
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message to JSON", e);
            throw new IllegalStateException("Failed to serialize message to JSON", e);
        }
    }

    @Override
    public String serializeToXml(Message message) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
            StringWriter writer = new StringWriter();
            marshaller.marshal(message, writer);
            String xml = writer.toString();
            log.debug("Serialized message to XML (chars={})", xml.length());
            return xml;
        } catch (JAXBException e) {
            log.error("Failed to serialize message to XML", e);
            throw new IllegalStateException("Failed to serialize message to XML", e);
        }
    }

    @Override
    public byte[] serializeToXmlUtf8(Message message) {
        return serializeToXml(message).getBytes(StandardCharsets.UTF_8);
    }
}
