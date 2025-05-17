package com.devfiles.enterprise.infrastructure.configuration.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public RedissonClient redisson() {
        var config = new Config();
        var serverConfig = config.useSingleServer();

        serverConfig.setAddress("redis://" + redisHost + ":" + redisPort);

        return Redisson.create(config);
    }
}
