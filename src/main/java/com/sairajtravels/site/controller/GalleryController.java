package com.sairajtravels.site.controller;

import com.sairajtravels.site.dto.GalleryDTO;
import com.sairajtravels.site.service.GalleryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/gallery")
@CrossOrigin(origins = "*")
public class GalleryController {
    
    private final GalleryService service;
    
    public GalleryController(GalleryService service) {
        this.service = service;
    }
    
    // Get all active gallery items for public display
    @GetMapping("/active")
    public ResponseEntity<List<GalleryDTO>> getActiveGallery() {
        try {
            List<GalleryDTO> gallery = service.getAllActiveGallery();
            return ResponseEntity.ok(gallery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get all gallery items for admin panel
    @GetMapping
    public ResponseEntity<List<GalleryDTO>> getAllGallery() {
        try {
            List<GalleryDTO> gallery = service.getAllGallery();
            return ResponseEntity.ok(gallery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get gallery items by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<GalleryDTO>> getGalleryByCategory(@PathVariable String category) {
        try {
            List<GalleryDTO> gallery = service.getGalleryByCategory(category);
            return ResponseEntity.ok(gallery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get featured gallery items
    @GetMapping("/featured")
    public ResponseEntity<List<GalleryDTO>> getFeaturedGallery() {
        try {
            List<GalleryDTO> gallery = service.getFeaturedGallery();
            return ResponseEntity.ok(gallery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get gallery images only
    @GetMapping("/images")
    public ResponseEntity<List<GalleryDTO>> getGalleryImages() {
        try {
            List<GalleryDTO> gallery = service.getGalleryImages();
            return ResponseEntity.ok(gallery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get gallery videos only
    @GetMapping("/videos")
    public ResponseEntity<List<GalleryDTO>> getGalleryVideos() {
        try {
            List<GalleryDTO> gallery = service.getGalleryVideos();
            return ResponseEntity.ok(gallery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get gallery item by ID
    @GetMapping("/{id}")
    public ResponseEntity<GalleryDTO> getGalleryById(@PathVariable Long id) {
        try {
            Optional<GalleryDTO> gallery = service.getGalleryById(id);
            return gallery.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Create new gallery item
    @PostMapping
    public ResponseEntity<GalleryDTO> createGallery(@RequestBody GalleryDTO gallery) {
        try {
            GalleryDTO created = service.createGallery(gallery);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Update existing gallery item
    @PutMapping("/{id}")
    public ResponseEntity<GalleryDTO> updateGallery(@PathVariable Long id, @RequestBody GalleryDTO gallery) {
        try {
            Optional<GalleryDTO> updated = service.updateGallery(id, gallery);
            return updated.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Delete gallery item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGallery(@PathVariable Long id) {
        try {
            boolean deleted = service.deleteGallery(id);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Toggle gallery item active status
    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<GalleryDTO> toggleActiveStatus(@PathVariable Long id) {
        try {
            Optional<GalleryDTO> updated = service.toggleActiveStatus(id);
            return updated.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Toggle gallery item featured status
    @PatchMapping("/{id}/toggle-featured")
    public ResponseEntity<GalleryDTO> toggleFeaturedStatus(@PathVariable Long id) {
        try {
            Optional<GalleryDTO> updated = service.toggleFeaturedStatus(id);
            return updated.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get gallery statistics
    @GetMapping("/stats")
    public ResponseEntity<GalleryService.GalleryStatsDTO> getGalleryStats() {
        try {
            GalleryService.GalleryStatsDTO stats = service.getGalleryStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
