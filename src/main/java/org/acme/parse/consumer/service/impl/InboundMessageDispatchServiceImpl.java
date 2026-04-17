package org.acme.parse.consumer.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.acme.parse.consumer.service.InboundMessageDispatchService;
import org.acme.parse.consumer.service.JsonQueueMessageService;
import org.acme.parse.consumer.service.XmlQueueMessageService;

@Service
@RequiredArgsConstructor
@Slf4j
public class InboundMessageDispatchServiceImpl implements InboundMessageDispatchService {

    private final JsonQueueMessageService jsonHandler;
    private final XmlQueueMessageService xmlHandler;

    @Override
    public void dispatch(String payload) {
        if (payload == null) {
            log.warn("Ignoring null queue payload");
            return;
        }

        String trimmed = payload.stripLeading();
        if (trimmed.startsWith("{") || trimmed.startsWith("[")) {
            jsonHandler.handle(payload);
        } else if (trimmed.startsWith("<")) {
            xmlHandler.handle(payload);
        } else {
            log.warn(
                    "Unknown queue message format (expected JSON object/array or XML); chars={}",
                    payload.length());
        }
    }
}
