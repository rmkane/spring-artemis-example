package org.acme.parse.apps.consumerxml.web;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

@Hidden
@Controller
public class HomeController {

    private final Environment environment;

    public HomeController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String home() {
        String appName = HtmlUtils.htmlEscape(
                environment.getProperty("spring.application.name", "application"));
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1">
                  <title>%1$s</title>
                  <style>
                    body { font-family: system-ui, sans-serif; margin: 2rem; line-height: 1.55; color: #1a1a1a; max-width: 40rem; }
                    header { border-bottom: 1px solid #ddd; padding-bottom: 1rem; margin-bottom: 1.5rem; }
                    h1 { margin: 0 0 0.25rem 0; font-size: 1.5rem; }
                    .tagline { margin: 0; color: #555; font-size: 0.95rem; }
                    main p { margin: 0.75rem 0; }
                    a { color: #0b57d0; }
                    .muted { color: #666; font-size: 0.9rem; }
                  </style>
                </head>
                <body>
                  <header>
                    <h1>%1$s</h1>
                    <p class="tagline">Spring Boot · JMS / Artemis</p>
                  </header>
                  <main>
                    <p>Use <strong>Swagger UI</strong> to call the REST APIs (publish, consume pause/resume, and OpenAPI examples).</p>
                    <p><a href="/swagger-ui/index.html">Open Swagger UI →</a></p>
                    <p class="muted"><a href="/v3/api-docs">OpenAPI document (JSON)</a></p>
                  </main>
                </body>
                </html>
                """
                .formatted(appName);
    }
}
