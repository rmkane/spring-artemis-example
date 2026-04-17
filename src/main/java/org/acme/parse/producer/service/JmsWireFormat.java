package org.acme.parse.producer.service;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "How the payload is encoded on the JMS TextMessage when published")
public enum JmsWireFormat {
    @Schema(description = "application/json string") json,
    @Schema(description = "application/xml string (Jakarta XML Binding)") xml
}
