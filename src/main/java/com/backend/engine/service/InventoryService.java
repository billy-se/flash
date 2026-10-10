package com.backend.engine.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class InventoryService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private DefaultRedisScript<Long> checkoutScript;


    public boolean checkAndDecrementStock(Long productId, int quantity) {
        String key = "product:" + productId + ":stock";

        Long result = redisTemplate.execute(checkoutScript, Collections.singletonList(key), String.valueOf(quantity));

        return result != null && result == 1;
    }
}