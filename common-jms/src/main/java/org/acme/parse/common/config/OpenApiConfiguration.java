package org.acme.parse.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Sets OpenAPI {@link Info#getTitle()} from {@code spring.application.name} so Swagger UI shows which
 * Boot app you are in (producer-json, consumer-xml, etc.).
 */
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openApi(Environment environment) {
        String applicationName = environment.getProperty("spring.application.name", "application");
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName)
                        .description("REST API for `" + applicationName + "` (JMS publish or consume lifecycle).")
                        .version("0.1.0-SNAPSHOT"));
    }
}
