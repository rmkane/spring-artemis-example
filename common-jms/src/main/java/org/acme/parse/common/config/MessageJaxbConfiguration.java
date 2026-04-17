package org.acme.parse.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import org.acme.parse.common.model.Message;

@Configuration
public class MessageJaxbConfiguration {

    @Bean
    public JAXBContext messageJaxbContext() {
        try {
            return JAXBContext.newInstance(Message.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Failed to create JAXBContext for Message", e);
        }
    }
}
