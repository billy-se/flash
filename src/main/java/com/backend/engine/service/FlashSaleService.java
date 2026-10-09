package com.backend.engine.service;

import com.backend.engine.model.FlashSaleOrder;
import com.backend.engine.model.Product;
import com.backend.engine.repository.OrderRepository;
import com.backend.engine.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlashSaleService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public FlashSaleService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public FlashSaleOrder purchaseProduct(Long productId, String userId, int quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new IllegalStateException("Out of stock!");
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        FlashSaleOrder order = new FlashSaleOrder();
        order.setProductId(productId);
        order.setUserId(userId);
        order.setQuantity(quantity);
        order.setStatus("SUCCESS");
        return orderRepository.save(order);
    }
}