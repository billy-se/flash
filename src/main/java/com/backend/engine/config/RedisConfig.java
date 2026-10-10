package com.backend.engine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class RedisConfig {

    @Bean
    public DefaultRedisScript<Long> checkoutScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(
            "local stock = tonumber(redis.call('get', KEYS[1])) " +
            "if not stock then return -1 end " +
            "local requested = tonumber(ARGV[1]) " +
            "if stock >= requested then " +
            "   redis.call('decrby', KEYS[1], requested) " +
            "   return 1 " +
            "else " +
            "   return 0 " +
            "end"
        );
        script.setResultType(Long.class);
        return script;
    }
}