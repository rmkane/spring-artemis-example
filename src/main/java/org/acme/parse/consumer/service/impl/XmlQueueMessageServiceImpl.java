package org.acme.parse.consumer.service.impl;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import org.acme.parse.consumer.service.XmlQueueMessageService;

@Service
@Slf4j
public class XmlQueueMessageServiceImpl implements XmlQueueMessageService {

    @Override
    public void handle(String body) {
        log.info("XML inbound message chars={}", body.length());
    }
}
