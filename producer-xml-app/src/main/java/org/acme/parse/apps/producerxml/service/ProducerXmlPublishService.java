package org.acme.parse.apps.producerxml.service;

import org.acme.parse.common.model.Message;
import org.acme.parse.jms.publish.service.JmsPublishResult;
import org.acme.parse.jms.publish.service.JmsWireFormat;

public interface ProducerXmlPublishService {

    JmsPublishResult publish(Message message, JmsWireFormat wireFormat);
}
