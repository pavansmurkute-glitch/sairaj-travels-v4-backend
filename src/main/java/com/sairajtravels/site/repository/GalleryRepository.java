package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    
    // Find all active gallery items ordered by sort order
    @Query("SELECT g FROM Gallery g WHERE g.isActive = true ORDER BY g.sortOrder ASC, g.createdAt ASC")
    List<Gallery> findAllActiveOrdered();
    
    // Find all gallery items ordered by sort order (for admin)
    @Query("SELECT g FROM Gallery g ORDER BY g.sortOrder ASC, g.createdAt ASC")
    List<Gallery> findAllOrdered();
    
    // Find gallery items by category
    List<Gallery> findByCategoryAndIsActiveTrueOrderBySortOrderAsc(String category);
    
    // Find featured gallery items
    List<Gallery> findByIsFeaturedTrueAndIsActiveTrueOrderBySortOrderAsc();
    
    // Find gallery items by type (image or video)
    @Query("SELECT g FROM Gallery g WHERE g.isActive = true AND g.imagePath IS NOT NULL AND g.imagePath != '' ORDER BY g.sortOrder ASC")
    List<Gallery> findActiveImagesOrdered();
    
    @Query("SELECT g FROM Gallery g WHERE g.isActive = true AND g.videoUrl IS NOT NULL AND g.videoUrl != '' ORDER BY g.sortOrder ASC")
    List<Gallery> findActiveVideosOrdered();
    
    // Count gallery items by category
    long countByCategoryAndIsActiveTrue(String category);
    
    // Count all active gallery items
    long countByIsActiveTrue();
}
