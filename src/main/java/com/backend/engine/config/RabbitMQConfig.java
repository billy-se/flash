package com.backend.engine.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

import java.util.List;

@Configuration
public class RabbitMQConfig {

    public static final String CHECKOUT_QUEUE = "checkoutQueue";

    @Bean
    public Queue checkoutQueue() {
        return new Queue(CHECKOUT_QUEUE, true);
    }

    @Bean
    public MessageConverter messageConverter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(List.of("com.backend.engine.model.*", "java.util.*"));
        return converter;
    }
}