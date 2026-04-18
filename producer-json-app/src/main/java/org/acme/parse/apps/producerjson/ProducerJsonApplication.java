package org.acme.parse.apps.producerjson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
            "org.acme.parse.common",
            "org.acme.parse.jms.publish",
            "org.acme.parse.apps.producerjson"
        })
public class ProducerJsonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerJsonApplication.class, args);
    }
}
