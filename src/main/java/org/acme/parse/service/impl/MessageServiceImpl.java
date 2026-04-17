package org.acme.parse.service.impl;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import org.acme.parse.model.Message;
import org.acme.parse.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JAXBContext jaxbContext;

    public MessageServiceImpl() {
        try {
            this.jaxbContext = JAXBContext.newInstance(Message.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Failed to create JAXBContext", e);
        }
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

    /**
     * UTF-8 bytes suitable for {@code jakarta.jms.BytesMessage}.
     */
    @Override
    public byte[] serializeToXmlUtf8(Message message) {
        return serializeToXml(message).getBytes(StandardCharsets.UTF_8);
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
