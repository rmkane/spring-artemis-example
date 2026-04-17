package org.acme.parse.common.service;

import org.acme.parse.common.model.Message;

public interface MessageService {

    String serializeToJson(Message message);

    String serializeToXml(Message message);

    byte[] serializeToXmlUtf8(Message message);

    Message deserializeFromXml(String xml);

    Message deserializeFromXmlUtf8(byte[] xmlUtf8);
}
