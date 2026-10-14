package com.backend.engine.controller;

import com.backend.engine.model.CheckoutRequest;
import com.backend.engine.service.RateLimiterService;
import com.backend.engine.queue.CheckoutQueueProducer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.data.redis.core.RedisTemplate;
import com.backend.engine.service.InventoryService;
import com.backend.engine.model.FlashSaleOrder;
import com.backend.engine.model.OrderRepository;
import java.time.Duration;

@RestController
@RequestMapping("/api")
public class CheckoutController {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private CheckoutQueueProducer queueProducer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/order/{userId")
    public ResponseEntity<?> getOrderStatus(@org.springframework.web.bind.annotation.PathVariable String userId) {
        List<FlashSaleOrder> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No orders found for this user");
        }
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestHeader("Idempotency-Key") String idempotencyKey, @RequestBody CheckoutRequest request, HttpServletRequest servletRequest) {
        String idempotencyRedisKey = "idempotency:" + idempotencyKey;

        Boolean isNewRequest = redisTemplate.opsForValue().setIfAbsent(idempotencyRedisKey, "PROCESSED", Duration.ofMinutes(10));
        
        if (Boolean.FALSE.equals(isNewRequest)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Duplicate request detected. This order is already in processed");
        }

        String clientIp = getClientIp(servletRequest);  

        if (!rateLimiterService.isAllowed(clientIp, request.getUserId())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Rate limit exceeded. Please try again later");
        }

        boolean stockAvailable = inventoryService.checkAndDecrementStock(request.getProductId(), request.getQuantity());
        if (!stockAvailable) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sorry, item is sold out!");
        }

        queueProducer.sendToQueue(request);

        return ResponseEntity.accepted()
            .body("You are in line! Request received successfully");
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}