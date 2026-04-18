package org.acme.parse.apps.producerjson.service;

import org.acme.parse.common.model.Message;
import org.acme.parse.jms.publish.service.JmsPublishResult;
import org.acme.parse.jms.publish.service.JmsWireFormat;

public interface ProducerJsonPublishService {

    JmsPublishResult publish(Message message, JmsWireFormat wireFormat);
}
