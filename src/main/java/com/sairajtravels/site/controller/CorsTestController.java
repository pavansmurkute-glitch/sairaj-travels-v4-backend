package com.sairajtravels.site.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cors-test")
@CrossOrigin(origins = "${app.cors.allowed-origins:*}")
public class CorsTestController {

    @Value("${app.cors.allowed-origins:*}")
    private String allowedOrigins;

    @GetMapping("/info")
    public ResponseEntity<Object> getCorsInfo() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .body(Map.of(
                    "message", "CORS test successful",
                    "allowedOrigins", allowedOrigins,
                    "timestamp", System.currentTimeMillis()
                ));
    }

    @GetMapping("/simple")
    public String simpleTest() {
        return "CORS working!";
    }
}
