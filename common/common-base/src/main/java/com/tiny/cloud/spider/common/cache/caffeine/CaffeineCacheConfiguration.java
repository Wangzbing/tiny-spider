package com.tiny.cloud.spider.common.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/04/06
 */
@Configuration
@Import(CaffeineCacheProperties.class)
@EnableCaching
public class CaffeineCacheConfiguration  {

    @Bean
    public Cache<String,Object> stringKeyCaffeineCache(CaffeineCacheProperties cacheConfig){
        return Caffeine.newBuilder()
                .initialCapacity(cacheConfig.getInitialCapacity())
                .softValues()
                .maximumSize(cacheConfig.getMaximumSize())
                .build();
    }
}
