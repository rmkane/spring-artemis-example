package org.acme.parse.consumer.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import org.acme.parse.consumer.service.InboundMessageDispatchService;

@Component
@RequiredArgsConstructor
public class QueueMessageListener {

    private final InboundMessageDispatchService dispatchService;

    @JmsListener(
            id = JmsListenerIds.MESSAGE_QUEUE,
            destination = "${app.jms.queue}",
            concurrency = "1")
    public void onMessage(String text) {
        dispatchService.dispatch(text);
    }
}
