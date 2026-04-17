package org.acme.parse.consumer.service.impl;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.acme.parse.consumer.service.JsonQueueMessageService;

@Service
@RequiredArgsConstructor
@Slf4j
public class JsonQueueMessageServiceImpl implements JsonQueueMessageService {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(String body) {
        try {
            JsonNode root = objectMapper.readTree(body);
            log.info(
                    "JSON inbound message fieldCount={} chars={}",
                    root.size(),
                    body.length());
        } catch (Exception e) {
            log.error("JSON inbound message could not be parsed (chars={})", body.length(), e);
        }
    }
}
