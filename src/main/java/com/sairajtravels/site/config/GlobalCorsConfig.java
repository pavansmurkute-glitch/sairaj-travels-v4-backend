package com.sairajtravels.site.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalCorsConfig {

    @Value("${app.cors.allowed-origins:*}")
    private String allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        System.out.println("Global CORS Configuration - Allowed Origins: " + allowedOrigins);
        
        // Allow all origins for now to test
        config.addAllowedOriginPattern("*");
        config.addAllowedOrigin("https://sairaj-travels-v5-frontend.onrender.com");
        config.addAllowedOrigin("https://sairaj-travels-v4-frontend.onrender.com");
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://localhost:3000");
        
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply to all paths, not just /api/**

        return new CorsFilter(source);
    }
}
