package com.devfiles.enterprise.infrastructure.adapter.gateway.cache;


import com.devfiles.enterprise.infrastructure.adapter.abstraction.CacheGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Qualifier("redisCacheGateway")
@Slf4j
public class RedisCacheGateway implements CacheGateway {
    private final RedisTemplate<String, Object> redisTemplate;
    private final long TTL_IN_MS;

    public RedisCacheGateway(
            RedisTemplate<String, Object> redisTemplate,
            @Value("${spring.cache.redis.time-to-live}") long ttl
    ) {
        this.redisTemplate = redisTemplate;
        this.TTL_IN_MS = ttl;
    }

    @Override
    public void put(String key, Object value) {
        internalPut(key, value, Duration.ofMillis(TTL_IN_MS));
    }

    @Override
    public void put(String key, Object value, Duration ttl) {
        internalPut(key, value, ttl);
    }

    @Override
    public Object get(String key) {
        var value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            log.info("Key '{}' not found in cache", key);
        } else {
            log.info("Key '{}' found in cache with value '{}'", key, value);
        }

        return value;
    }

    private void internalPut(String key, Object value, Duration ttl) {
        if (value == null) {
            log.warn("Trying to put null value in cache with key '{}' and ttl '{}'", key, ttl);
            return;
        }

        log.info("Putting key '{}' with value '{}' in cache with ttl '{}'", key, value, ttl);
        redisTemplate.opsForValue().set(key, value, ttl);
    }
}