package com.example.accessingdatarest;

import jakarta.jms.ConnectionFactory;
import lombok.SneakyThrows;
import org.apache.activemq.artemis.core.config.WildcardConfiguration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.apache.activemq.artemis.core.settings.impl.AddressSettings;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class JmsConfig {
    @SneakyThrows
    @Bean(initMethod = "start", destroyMethod = "stop")
    public EmbeddedActiveMQ embeddedActiveMQ() {
        EmbeddedActiveMQ embeddedActiveMQ = new EmbeddedActiveMQ();
        org.apache.activemq.artemis.core.config.Configuration artemisConfig = new ConfigurationImpl();
        //artemisConfig.setWildCardConfiguration(new WildcardConfiguration().setDelimiter('/'));
        artemisConfig.addAcceptorConfiguration("tcp", "tcp://localhost:61613");
        //artemisConfig.addAddressSetting("/topic/#",new AddressSettings().setRetroactiveMessageCount(100));
        artemisConfig.setPersistenceEnabled(false);
        artemisConfig.setSecurityEnabled(false);
        embeddedActiveMQ.setConfiguration(artemisConfig);
        return embeddedActiveMQ;
    }

    @Bean
    public CachingConnectionFactory connectionFactory(EmbeddedActiveMQ embeddedActiveMQ) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61613");
        return new CachingConnectionFactory(connectionFactory);
    }

    @Bean
    public JmsTemplate jmsTemplate(CachingConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true); // Enable for topics
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(CachingConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true); // Enable for topics
        return factory;
    }
}
