package org.acme.parse.web;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "How the message body is encoded on the JMS TextMessage")
public enum JmsWireFormat {
    @Schema(description = "application/json string") json,
    @Schema(description = "application/xml string (Jakarta XML Binding)") xml
}
