package com.backend.engine.queue;

import com.backend.engine.config.RabbitMQConfig;
import com.backend.engine.model.CheckoutRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckoutQueueProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendToQueue(CheckoutRequest request) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.CHECKOUT_QUEUE, request);
    }
}