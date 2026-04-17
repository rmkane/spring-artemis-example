package org.acme.parse.consumer.service;

public interface JsonQueueMessageService {

    void handle(String body);
}
