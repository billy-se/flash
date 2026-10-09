package com.backend.engine.config;

import com.backend.engine.config.RabbitMQConfig;    
import com.backend.engine.model.CheckoutRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.backend.engine.service.FlashSaleService;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class CheckoutQueueConsumer {

    @Autowired
    private FlashSaleService flashSaleService;

    @RabbitListener(queues = "checkoutQueue")
    public void receiveMessage(CheckoutRequest request) {
        flashSaleService.purchaseProduct(request.getProductId(), request.getUserId(), request.getQuantity());
        System.out.println("Processing checkout for User: " + request.getUserId());
    }
}