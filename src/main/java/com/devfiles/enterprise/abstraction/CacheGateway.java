package com.devfiles.enterprise.abstraction;

import java.time.Duration;

public interface CacheGateway {
    void put(String key, Object value);
    void put(String key, Object value, Duration ttl);
    Object get(String key);
}