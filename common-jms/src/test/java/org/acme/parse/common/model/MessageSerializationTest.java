package org.acme.parse.common.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import org.acme.parse.common.service.MessageSerializationService;
import org.acme.parse.common.service.impl.MessageSerializationServiceImpl;

/**
 * Asserts on parsed trees ({@link JsonNode}), not unmarshalling back into the original POJO.
 */
class MessageSerializationTest {

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final XmlMapper XML = new XmlMapper();

    private final MessageSerializationService serializationService;

    MessageSerializationTest() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Message.class);
        this.serializationService = new MessageSerializationServiceImpl(JSON, jaxbContext);
    }

    @Test
    void json_payload_parses_to_tree_and_queries_values() throws Exception {
        Message message = sampleMessage();
        String payload = serializationService.serializeToJson(message);

        JsonNode root = JSON.readTree(payload);

        assertEquals("1.0", root.at("/header/version").asText());
        assertEquals("2026-04-16T12:00:00Z", root.at("/header/timestamp").asText());
        assertEquals("sender", root.at("/header/sender").asText());
        assertEquals("recipient", root.at("/header/recipient").asText());
        assertEquals("content", root.at("/body/content").asText());
    }

    @Test
    void xml_payload_parses_to_tree_and_queries_values() throws Exception {
        Message message = sampleMessage();
        String payload = serializationService.serializeToXml(message);

        JsonNode root = XML.readTree(payload);

        assertEquals("1.0", root.at("/header/version").asText());
        assertEquals("2026-04-16T12:00:00Z", root.at("/header/timestamp").asText());
        assertEquals("sender", root.at("/header/sender").asText());
        assertEquals("recipient", root.at("/header/recipient").asText());
        assertEquals("content", root.at("/body/content").asText());
    }

    @Test
    void xml_utf8_bytes_parse_same_as_string() throws Exception {
        Message message = sampleMessage();
        byte[] utf8 = serializationService.serializeToXmlUtf8(message);

        JsonNode fromBytes = XML.readTree(new String(utf8, StandardCharsets.UTF_8));
        assertEquals("content", fromBytes.at("/body/content").asText());
    }

    private static Message sampleMessage() {
        Message message = new Message();
        Header header = new Header();
        header.setVersion("1.0");
        header.setTimestamp("2026-04-16T12:00:00Z");
        header.setSender("sender");
        header.setRecipient("recipient");
        message.setHeader(header);
        Body body = new Body();
        body.setContent("content");
        message.setBody(body);
        return message;
    }
}
