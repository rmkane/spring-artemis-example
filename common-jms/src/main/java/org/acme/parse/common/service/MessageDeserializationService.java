package org.acme.parse.common.service;

import org.acme.parse.common.model.Message;

/** Deserialize / unmarshal {@link Message} from XML wire form. */
public interface MessageDeserializationService {

    Message deserializeFromXml(String xml);

    Message deserializeFromXmlUtf8(byte[] xmlUtf8);
}
