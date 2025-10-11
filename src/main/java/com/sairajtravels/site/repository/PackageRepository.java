package com.sairajtravels.site.repository;

import com.sairajtravels.site.entity.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<TravelPackage, Integer> {
    
    // Find all active packages - prioritize featured packages, then by sort order
    @Query("SELECT p FROM TravelPackage p WHERE p.isActive = true ORDER BY " +
           "CASE WHEN p.isFeatured = true THEN 0 ELSE 1 END, " +
           "p.sortOrder ASC, p.packageId ASC")
    List<TravelPackage> findByIsActiveTrueOrderByFeaturedAndSortOrder();
    
    // Find packages by category
    List<TravelPackage> findByPackageCategoryIdAndIsActiveTrueOrderBySortOrderAsc(String categoryId);
    
    // Find featured packages
    List<TravelPackage> findByIsFeaturedTrueAndIsActiveTrueOrderBySortOrderAsc();
    
    // Search packages by name or description
    @Query("SELECT p FROM TravelPackage p WHERE p.isActive = true AND " +
           "(LOWER(p.packageName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.packageDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY p.sortOrder ASC")
    List<TravelPackage> searchPackages(@Param("searchTerm") String searchTerm);
    
    // Find packages by price range
    @Query("SELECT p FROM TravelPackage p WHERE p.isActive = true AND " +
           "p.packagePrice BETWEEN :minPrice AND :maxPrice " +
           "ORDER BY p.sortOrder ASC")
    List<TravelPackage> findByPriceRange(@Param("minPrice") java.math.BigDecimal minPrice, 
                                  @Param("maxPrice") java.math.BigDecimal maxPrice);
    
    // Find packages by category and price range
    @Query("SELECT p FROM TravelPackage p WHERE p.isActive = true AND " +
           "p.packageCategoryId = :categoryId AND " +
           "p.packagePrice BETWEEN :minPrice AND :maxPrice " +
           "ORDER BY p.sortOrder ASC")
    List<TravelPackage> findByCategoryAndPriceRange(@Param("categoryId") String categoryId,
                                             @Param("minPrice") java.math.BigDecimal minPrice,
                                             @Param("maxPrice") java.math.BigDecimal maxPrice);
}
