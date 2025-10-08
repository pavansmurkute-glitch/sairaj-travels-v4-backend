package com.sairajtravels.site.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/database")
    public String testDatabase() {
        return "Database connection test - Backend is running!";
    }
}
