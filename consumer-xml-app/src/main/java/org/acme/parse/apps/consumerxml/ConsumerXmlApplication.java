package org.acme.parse.apps.consumerxml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
            "org.acme.parse.common",
            "org.acme.parse.jms.consumer",
            "org.acme.parse.apps.consumerxml"
        })
public class ConsumerXmlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerXmlApplication.class, args);
    }
}
