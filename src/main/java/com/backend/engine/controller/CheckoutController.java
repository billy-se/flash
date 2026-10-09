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

@RestController
@RequestMapping("/api")
public class CheckoutController {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private CheckoutQueueProducer queueProducer;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request, HttpServletRequest servletRequest) {
        String clientIp = getClientIp(servletRequest);

        if (!rateLimiterService.isAllowed(clientIp, request.getUserId())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Rate limit exceeded. Please try again later");
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