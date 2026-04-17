package org.acme.parse.common.boot;

import java.util.Optional;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Logs a single startup line with the bound HTTP port once the web server is ready.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WebServerStartupLogger implements ApplicationListener<WebServerInitializedEvent> {

    private final Environment environment;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        String applicationName =
                Optional.ofNullable(environment.getProperty("spring.application.name")).orElse("application");
        String bindAddress = Optional.ofNullable(environment.getProperty("server.address"))
                .filter(a -> !a.isBlank())
                .orElse(null);
        String logHost = displayHost(bindAddress);

        log.info(
                "Started server [{}] at http://{}:{} (listening on {})",
                applicationName,
                logHost,
                port,
                bindAddress == null ? "0.0.0.0:" + port : bindAddress + ":" + port);
    }

    private static String displayHost(String bindAddress) {
        if (bindAddress == null || "0.0.0.0".equals(bindAddress) || "::".equals(bindAddress)) {
            return "localhost";
        }
        return bindAddress;
    }
}
