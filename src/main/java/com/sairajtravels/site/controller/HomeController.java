package com.sairajtravels.site.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Sairaj Travels Backend API");
        response.put("status", "UP");
        response.put("version", "1.0.0");
        response.put("endpoints", Map.of(
            "health", "/actuator/health",
            "api", "/api",
            "swagger", "/swagger-ui.html"
        ));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api")
    public ResponseEntity<Map<String, Object>> api() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Sairaj Travels API");
        response.put("status", "Available");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Backend is working!");
        response.put("status", "SUCCESS");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}
