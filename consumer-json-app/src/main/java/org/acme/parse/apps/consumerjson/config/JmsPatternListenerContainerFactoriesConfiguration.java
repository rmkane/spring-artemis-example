package org.acme.parse.apps.consumerjson.config;

import jakarta.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

/**
 * Examples of the three common JMS receive patterns:
 *
 * <ul>
 *   <li><strong>Queue</strong> — point-to-point; messages wait on the broker until a consumer takes them.</li>
 *   <li><strong>Topic (non-durable)</strong> — publish/subscribe; only active subscribers receive messages.</li>
 *   <li><strong>Topic (durable)</strong> — same as topic, but the broker retains messages for an inactive named
 *       subscription (requires {@code clientId} + subscription name).</li>
 * </ul>
 */
@Configuration
public class JmsPatternListenerContainerFactoriesConfiguration {

    @Bean
    public DefaultJmsListenerContainerFactory queueJmsListenerContainerFactory(
            ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setPubSubDomain(false);
        factory.setSubscriptionDurable(false);
        factory.setClientId(null);
        factory.setAutoStartup(true);
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory topicNonDurableJmsListenerContainerFactory(
            ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setPubSubDomain(true);
        factory.setSubscriptionDurable(false);
        factory.setClientId(null);
        factory.setAutoStartup(true);
        return factory;
    }

    /**
     * Durable demo topic: same subscription-durable flag as the default factory, but a distinct JMS client id
     * ({@code app.jms.demo.durable-client-id}) from {@code spring.jms.client-id}. The broker allows only one
     * active connection per client id; the main listener and this demo cannot both use {@code consumer-json-app}.
     */
    @Bean
    public DefaultJmsListenerContainerFactory durableTopicDemoJmsListenerContainerFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer,
            @Value("${app.jms.demo.durable-client-id}") String demoDurableClientId) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setClientId(demoDurableClientId);
        factory.setAutoStartup(true);
        return factory;
    }
}
