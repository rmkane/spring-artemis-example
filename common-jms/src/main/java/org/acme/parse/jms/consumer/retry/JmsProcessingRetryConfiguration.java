package org.acme.parse.jms.consumer.retry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class JmsProcessingRetryConfiguration {

    @Bean
    public RetryTemplate jmsProcessingRetryTemplate(
            @Value("${app.jms.retry.max-attempts:3}") int maxAttempts,
            @Value("${app.jms.retry.initial-interval-ms:200}") long initialIntervalMs,
            @Value("${app.jms.retry.multiplier:2.0}") double multiplier) {

        RetryTemplate template = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(Math.max(1, maxAttempts));
        template.setRetryPolicy(retryPolicy);

        ExponentialBackOffPolicy backOff = new ExponentialBackOffPolicy();
        backOff.setInitialInterval(Math.max(1L, initialIntervalMs));
        backOff.setMultiplier(Math.max(1.0, multiplier));
        template.setBackOffPolicy(backOff);

        return template;
    }
}
