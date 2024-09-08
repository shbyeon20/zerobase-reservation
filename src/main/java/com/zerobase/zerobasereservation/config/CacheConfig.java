package com.zerobase.zerobasereservation.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

@RequiredArgsConstructor
public class CacheConfig {

    @Value("${data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration conf =
                new RedisStandaloneConfiguration(host, port);


        return null;
    }

}
