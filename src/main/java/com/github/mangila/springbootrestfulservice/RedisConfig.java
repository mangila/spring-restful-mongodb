package com.github.mangila.springbootrestfulservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.springbootrestfulservice.web.dto.v1.CustomerDto;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    private final ObjectMapper mapper;

    public RedisConfig(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        var jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(CustomerDto.class);
        jackson2JsonRedisSerializer.setObjectMapper(this.mapper);
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
    }
}
