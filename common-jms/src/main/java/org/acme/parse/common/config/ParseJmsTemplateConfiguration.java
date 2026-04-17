package org.acme.parse.common.config;

import jakarta.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.core.JmsTemplate;

/**
 * Primary {@link JmsTemplate} publishes and consumes JMS <strong>topics</strong> ({@code pubSubDomain=true}).
 * {@code queueJmsTemplate} is point-to-point for the dead-letter <strong>queue</strong> only.
 */
@Configuration
public class ParseJmsTemplateConfiguration {

    @Bean
    @Primary
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setPubSubDomain(true);
        return template;
    }

    @Bean(name = "queueJmsTemplate")
    public JmsTemplate queueJmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setPubSubDomain(false);
        return template;
    }
}
