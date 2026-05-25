package com.teamates.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// FLAW: uses in-memory ConcurrentMapCache — not suitable for production or clustered environments
// FLAW: no TTL configured — cache entries live forever
// FLAW: cache keys not defined — relies on default key strategy which may collide
@Configuration
@EnableCaching
public class CacheConfiguration {

    // FLAW: no eviction policy, no TTL — memory will grow unbounded
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("products", "orders", "reviews");
        // FLAW: "customers" cache not included but CustomerService may need it
    }
}
