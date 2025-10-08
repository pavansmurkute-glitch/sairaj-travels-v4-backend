package com.sairajtravels.site.service;

import com.sairajtravels.site.dto.GalleryDTO;
import com.sairajtravels.site.entity.Gallery;
import com.sairajtravels.site.repository.GalleryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GalleryService {
    
    private final GalleryRepository repository;
    
    public GalleryService(GalleryRepository repository) {
        this.repository = repository;
    }
    
    // Get all active gallery items for public display
    public List<GalleryDTO> getAllActiveGallery() {
        List<Gallery> gallery = repository.findAllActiveOrdered();
        return gallery.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get all gallery items for admin panel
    public List<GalleryDTO> getAllGallery() {
        List<Gallery> gallery = repository.findAllOrdered();
        return gallery.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get gallery items by category
    public List<GalleryDTO> getGalleryByCategory(String category) {
        List<Gallery> gallery = repository.findByCategoryAndIsActiveTrueOrderBySortOrderAsc(category);
        return gallery.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get featured gallery items
    public List<GalleryDTO> getFeaturedGallery() {
        List<Gallery> gallery = repository.findByIsFeaturedTrueAndIsActiveTrueOrderBySortOrderAsc();
        return gallery.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get gallery images only
    public List<GalleryDTO> getGalleryImages() {
        List<Gallery> gallery = repository.findActiveImagesOrdered();
        return gallery.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get gallery videos only
    public List<GalleryDTO> getGalleryVideos() {
        List<Gallery> gallery = repository.findActiveVideosOrdered();
        return gallery.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get gallery item by ID
    public Optional<GalleryDTO> getGalleryById(Long id) {
        return repository.findById(id)
                .map(this::convertToDTO);
    }
    
    // Create new gallery item
    public GalleryDTO createGallery(GalleryDTO dto) {
        Gallery gallery = convertToEntity(dto);
        gallery = repository.save(gallery);
        return convertToDTO(gallery);
    }
    
    // Update existing gallery item
    public Optional<GalleryDTO> updateGallery(Long id, GalleryDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setTitle(dto.getTitle());
                    existing.setDescription(dto.getDescription());
                    existing.setImagePath(dto.getImagePath());
                    existing.setVideoUrl(dto.getVideoUrl());
                    existing.setCategory(dto.getCategory());
                    existing.setIsFeatured(dto.getIsFeatured());
                    existing.setIsActive(dto.getIsActive());
                    existing.setSortOrder(dto.getSortOrder());
                    existing = repository.save(existing);
                    return convertToDTO(existing);
                });
    }
    
    // Delete gallery item
    public boolean deleteGallery(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Toggle gallery item active status
    public Optional<GalleryDTO> toggleActiveStatus(Long id) {
        return repository.findById(id)
                .map(gallery -> {
                    gallery.setIsActive(!gallery.getIsActive());
                    gallery = repository.save(gallery);
                    return convertToDTO(gallery);
                });
    }
    
    // Toggle gallery item featured status
    public Optional<GalleryDTO> toggleFeaturedStatus(Long id) {
        return repository.findById(id)
                .map(gallery -> {
                    gallery.setIsFeatured(!gallery.getIsFeatured());
                    gallery = repository.save(gallery);
                    return convertToDTO(gallery);
                });
    }
    
    // Get gallery statistics
    public GalleryStatsDTO getGalleryStats() {
        long totalItems = repository.countByIsActiveTrue();
        long fleetCount = repository.countByCategoryAndIsActiveTrue("Fleet");
        long luxuryCount = repository.countByCategoryAndIsActiveTrue("Luxury");
        long destinationsCount = repository.countByCategoryAndIsActiveTrue("Destinations");
        long happyCustomersCount = repository.countByCategoryAndIsActiveTrue("Happy Customers");
        long interiorsCount = repository.countByCategoryAndIsActiveTrue("Interiors");
        long driversCount = repository.countByCategoryAndIsActiveTrue("Drivers");
        
        return new GalleryStatsDTO(totalItems, fleetCount, luxuryCount, destinationsCount, 
                                 happyCustomersCount, interiorsCount, driversCount);
    }
    
    // Convert Entity to DTO
    private GalleryDTO convertToDTO(Gallery gallery) {
        GalleryDTO dto = new GalleryDTO();
        dto.setId(gallery.getId());
        dto.setTitle(gallery.getTitle());
        dto.setDescription(gallery.getDescription());
        dto.setImagePath(gallery.getImagePath());
        dto.setVideoUrl(gallery.getVideoUrl());
        dto.setCategory(gallery.getCategory());
        dto.setIsFeatured(gallery.getIsFeatured());
        dto.setIsActive(gallery.getIsActive());
        dto.setSortOrder(gallery.getSortOrder());
        dto.setCreatedAt(gallery.getCreatedAt());
        dto.setUpdatedAt(gallery.getUpdatedAt());
        return dto;
    }
    
    // Convert DTO to Entity
    private Gallery convertToEntity(GalleryDTO dto) {
        Gallery gallery = new Gallery();
        gallery.setId(dto.getId());
        gallery.setTitle(dto.getTitle());
        gallery.setDescription(dto.getDescription());
        gallery.setImagePath(dto.getImagePath());
        gallery.setVideoUrl(dto.getVideoUrl());
        gallery.setCategory(dto.getCategory());
        gallery.setIsFeatured(dto.getIsFeatured());
        gallery.setIsActive(dto.getIsActive());
        gallery.setSortOrder(dto.getSortOrder());
        return gallery;
    }
    
    // Inner class for gallery statistics
    public static class GalleryStatsDTO {
        private long totalItems;
        private long fleetCount;
        private long luxuryCount;
        private long destinationsCount;
        private long happyCustomersCount;
        private long interiorsCount;
        private long driversCount;
        
        public GalleryStatsDTO(long totalItems, long fleetCount, long luxuryCount, 
                             long destinationsCount, long happyCustomersCount, 
                             long interiorsCount, long driversCount) {
            this.totalItems = totalItems;
            this.fleetCount = fleetCount;
            this.luxuryCount = luxuryCount;
            this.destinationsCount = destinationsCount;
            this.happyCustomersCount = happyCustomersCount;
            this.interiorsCount = interiorsCount;
            this.driversCount = driversCount;
        }
        
        // Getters
        public long getTotalItems() { return totalItems; }
        public long getFleetCount() { return fleetCount; }
        public long getLuxuryCount() { return luxuryCount; }
        public long getDestinationsCount() { return destinationsCount; }
        public long getHappyCustomersCount() { return happyCustomersCount; }
        public long getInteriorsCount() { return interiorsCount; }
        public long getDriversCount() { return driversCount; }
    }
}
