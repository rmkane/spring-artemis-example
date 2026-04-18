package org.acme.parse.apps.producerxml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
            "org.acme.parse.common",
            "org.acme.parse.jms.publish",
            "org.acme.parse.apps.producerxml"
        })
public class ProducerXmlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerXmlApplication.class, args);
    }
}
