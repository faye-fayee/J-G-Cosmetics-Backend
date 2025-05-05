package com.jgcosmetics.store.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://127.0.0.1:5500") // Live Server
                .allowedOrigins("http://localhost:8080") // Local HTML
                .allowedOrigins("http://34.105.57.169") // VM External IP
                .allowedOrigins("http://34.105.57.169:8080") // Online Server
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}
