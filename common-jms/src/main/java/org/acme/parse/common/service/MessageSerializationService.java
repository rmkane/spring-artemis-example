package org.acme.parse.common.service;

import org.acme.parse.common.model.Message;

/** Serialize / marshal {@link Message} to JSON or XML wire forms. */
public interface MessageSerializationService {

    String serializeToJson(Message message);

    String serializeToXml(Message message);

    byte[] serializeToXmlUtf8(Message message);
}
