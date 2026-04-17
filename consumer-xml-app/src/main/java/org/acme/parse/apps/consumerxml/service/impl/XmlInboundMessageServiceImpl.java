package org.acme.parse.apps.consumerxml.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.acme.parse.apps.consumerxml.service.XmlInboundMessageService;
import org.acme.parse.common.model.Message;
import org.acme.parse.common.service.MessageDeserializationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class XmlInboundMessageServiceImpl implements XmlInboundMessageService {

    private final MessageDeserializationService messageDeserializationService;

    @Override
    public void handle(String body) throws Exception {
        Message parsed = messageDeserializationService.deserializeFromXml(body);
        log.info(
                "XML inbound message sender={} chars={}",
                parsed.getHeader() != null ? parsed.getHeader().getSender() : null,
                body.length());
    }
}
