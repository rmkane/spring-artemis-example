package org.acme.parse.apps.consumerjson.service;

public interface JsonInboundMessageService {

    void handle(String body) throws Exception;
}
