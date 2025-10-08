package com.sairajtravels.site.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        
        // Define cache names for different data types
        cacheManager.setCacheNames(java.util.Arrays.asList(
            "packages",           // Travel packages cache
            "vehicles",           // Vehicle data cache
            "gallery",            // Gallery images cache
            "testimonials",       // Testimonials cache
            "contact",            // Contact information cache
            "adminUsers",         // Admin users cache (short TTL)
            "bookingStats"        // Booking statistics cache
        ));
        
        // Allow dynamic cache creation
        cacheManager.setAllowNullValues(false);
        
        return cacheManager;
    }
}
