package org.acme.parse.jms.publish.web;

/**
 * Example payloads for OpenAPI / Swagger UI (Try it out).
 */
public final class MessageApiExamples {

    public static final String JSON_ORDER =
            """
            {
              "header": {
                "version": "1.0",
                "timestamp": "2026-04-16T12:00:00Z",
                "sender": "orders",
                "recipient": "warehouse"
              },
              "body": {
                "content": "Ship 2x widget SKU-42 to dock B"
              }
            }
            """;

    public static final String JSON_STATUS =
            """
            {
              "header": {
                "version": "1.0",
                "timestamp": "2026-04-16T15:30:00Z",
                "sender": "monitoring",
                "recipient": "ops"
              },
              "body": {
                "content": "Health check OK for node-7"
              }
            }
            """;

    public static final String XML_ORDER =
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <message>
              <header>
                <version>1.0</version>
                <timestamp>2026-04-16T12:00:00Z</timestamp>
                <sender>orders</sender>
                <recipient>warehouse</recipient>
              </header>
              <body>
                <content>Ship 2x widget SKU-42 to dock B</content>
              </body>
            </message>
            """;

    public static final String XML_STATUS =
            """
            <?xml version="1.0" encoding="UTF-8"?>
            <message>
              <header>
                <version>1.0</version>
                <timestamp>2026-04-16T15:30:00Z</timestamp>
                <sender>monitoring</sender>
                <recipient>ops</recipient>
              </header>
              <body>
                <content>Health check OK for node-7</content>
              </body>
            </message>
            """;

    private MessageApiExamples() {}
}
