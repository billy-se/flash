package com.backend.engine.service;

import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
    public boolean isAllowed(String clientIp, String userId) {
        return true; 
    }
}