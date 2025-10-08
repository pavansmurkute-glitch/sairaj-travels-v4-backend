package com.sairajtravels.site.controller;

import com.sairajtravels.site.dto.TestimonialDTO;
import com.sairajtravels.site.service.TestimonialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/testimonials")
@CrossOrigin(origins = "*")
public class TestimonialController {
    
    private final TestimonialService service;
    
    public TestimonialController(TestimonialService service) {
        this.service = service;
    }
    
    // Get all active testimonials for public display
    @GetMapping("/active")
    public ResponseEntity<List<TestimonialDTO>> getActiveTestimonials() {
        try {
            List<TestimonialDTO> testimonials = service.getAllActiveTestimonials();
            return ResponseEntity.ok(testimonials);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get all testimonials for admin panel
    @GetMapping
    public ResponseEntity<List<TestimonialDTO>> getAllTestimonials() {
        try {
            List<TestimonialDTO> testimonials = service.getAllTestimonials();
            return ResponseEntity.ok(testimonials);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get testimonial by ID
    @GetMapping("/{id}")
    public ResponseEntity<TestimonialDTO> getTestimonialById(@PathVariable Long id) {
        try {
            Optional<TestimonialDTO> testimonial = service.getTestimonialById(id);
            return testimonial.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Create new testimonial
    @PostMapping
    public ResponseEntity<TestimonialDTO> createTestimonial(@RequestBody TestimonialDTO testimonial) {
        try {
            TestimonialDTO created = service.createTestimonial(testimonial);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Update existing testimonial
    @PutMapping("/{id}")
    public ResponseEntity<TestimonialDTO> updateTestimonial(@PathVariable Long id, @RequestBody TestimonialDTO testimonial) {
        try {
            Optional<TestimonialDTO> updated = service.updateTestimonial(id, testimonial);
            return updated.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Delete testimonial
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestimonial(@PathVariable Long id) {
        try {
            boolean deleted = service.deleteTestimonial(id);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Toggle testimonial active status
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TestimonialDTO> toggleActiveStatus(@PathVariable Long id) {
        try {
            Optional<TestimonialDTO> updated = service.toggleActiveStatus(id);
            return updated.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
