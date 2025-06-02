package com.weatherfit.comment_service.common;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisHealthCheck {

    public RedisHealthCheck(ReactiveRedisTemplate<String, String> redis) {
        redis.opsForValue().set("ping", "pong").thenMany(
                redis.opsForValue().get("ping")
        ).subscribe(v -> System.out.println("Redis says: " + v));
    }
}
