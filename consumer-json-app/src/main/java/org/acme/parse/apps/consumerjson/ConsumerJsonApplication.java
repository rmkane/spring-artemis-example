package org.acme.parse.apps.consumerjson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
            "org.acme.parse.common",
            "org.acme.parse.jms.consumer",
            "org.acme.parse.apps.consumerjson"
        })
public class ConsumerJsonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerJsonApplication.class, args);
    }
}
