package org.acme.parse.apps.consumerxml.service;

public interface XmlInboundMessageService {

    void handle(String body) throws Exception;
}
