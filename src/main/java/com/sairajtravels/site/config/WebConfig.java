package com.sairajtravels.site.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Serve static images from the resources/static/images directory
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
        
        // Also serve from the target/classes/static/images directory (for development)
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + Paths.get("target/classes/static/images/").toAbsolutePath().toString() + "/");
    }
}
