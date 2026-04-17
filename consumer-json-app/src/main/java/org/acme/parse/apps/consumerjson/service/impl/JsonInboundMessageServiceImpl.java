package org.acme.parse.apps.consumerjson.service.impl;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.acme.parse.apps.consumerjson.service.JsonInboundMessageService;

@Service
@RequiredArgsConstructor
@Slf4j
public class JsonInboundMessageServiceImpl implements JsonInboundMessageService {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(String body) throws Exception {
        JsonNode root = objectMapper.readTree(body);
        log.info(
                "JSON inbound message fieldCount={} chars={}",
                root.size(),
                body.length());
    }
}
