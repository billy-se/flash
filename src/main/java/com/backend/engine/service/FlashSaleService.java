package com.backend.engine.service;

import com.backend.engine.model.FlashSaleOrder;
import com.backend.engine.model.Product;
import com.backend.engine.repository.OrderRepository;
import com.backend.engine.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backend.engine.service.InventoryService;

@Service
public class FlashSaleService {

    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;

    public FlashSaleService(OrderRepository orderRepository, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public FlashSaleOrder purchaseProduct(Long productId, String userId, int quantity) {
        try {
            FlashSaleOrder order = new FlashSaleOrder();
            order.setProductId(productId);
            order.setUserId(userId);
            order.setQuantity(quantity);
            order.setStatus("SUCCESS");
            return orderRepository.save(order);
        } catch (Exception e) {
            inventoryService.restoreStock(String.valueOf(productId), quantity);
            throw new RuntimeException("Database error, stock restored in Redis", e);
        }
    }
}