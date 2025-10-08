package com.sairajtravels.site.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "service", "Sairaj Travels Backend",
            "cors", "enabled"
        ));
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        return ResponseEntity.ok(Map.of(
            "message", "Sairaj Travels Backend API",
            "status", "running",
            "timestamp", LocalDateTime.now()
        ));
    }
}
