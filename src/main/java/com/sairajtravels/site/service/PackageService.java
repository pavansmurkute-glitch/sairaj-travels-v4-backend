package com.sairajtravels.site.service;

import com.sairajtravels.site.dto.PackageDTO;
import com.sairajtravels.site.entity.TravelPackage;
import com.sairajtravels.site.entity.Vehicle;
import com.sairajtravels.site.repository.PackageRepository;
import com.sairajtravels.site.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;

    // Get all active packages (for public use) - with caching
    @Cacheable(value = "packages", key = "'active'")
    public List<PackageDTO> getAllPackages() {
        List<TravelPackage> packages = packageRepository.findByIsActiveTrueOrderBySortOrderAsc();
        return packages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get all packages including inactive ones (for admin use) - with pagination
    public List<PackageDTO> getAllPackagesForAdmin() {
        return getAllPackagesForAdmin(0, 50); // Default pagination
    }
    
    // Get all packages including inactive ones (for admin use) - with pagination
    public List<PackageDTO> getAllPackagesForAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("sortOrder"), Sort.Order.asc("packageId")));
        Page<TravelPackage> packagePage = packageRepository.findAll(pageable);
        
        return packagePage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get packages by category
    public List<PackageDTO> getPackagesByCategory(String categoryId) {
        List<TravelPackage> packages = packageRepository.findByPackageCategoryIdAndIsActiveTrueOrderBySortOrderAsc(categoryId);
        return packages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get featured packages - with caching
    @Cacheable(value = "packages", key = "'featured'")
    public List<PackageDTO> getFeaturedPackages() {
        List<TravelPackage> packages = packageRepository.findByIsFeaturedTrueAndIsActiveTrueOrderBySortOrderAsc();
        return packages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Search packages
    public List<PackageDTO> searchPackages(String searchTerm) {
        List<TravelPackage> packages = packageRepository.searchPackages(searchTerm);
        return packages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get packages by price range
    public List<PackageDTO> getPackagesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<TravelPackage> packages = packageRepository.findByPriceRange(minPrice, maxPrice);
        return packages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get packages by category and price range
    public List<PackageDTO> getPackagesByCategoryAndPriceRange(String categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        List<TravelPackage> packages = packageRepository.findByCategoryAndPriceRange(categoryId, minPrice, maxPrice);
        return packages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get package by ID
    public PackageDTO getPackageById(Integer packageId) {
        Optional<TravelPackage> packageOpt = packageRepository.findById(packageId);
        return packageOpt.map(this::convertToDTO).orElse(null);
    }

    // Create new package - clear cache
    @CacheEvict(value = "packages", allEntries = true)
    public PackageDTO createPackage(PackageDTO packageDTO) {
        TravelPackage packageEntity = convertToEntity(packageDTO);
        TravelPackage savedPackage = packageRepository.save(packageEntity);
        return convertToDTO(savedPackage);
    }

    // Update package - clear cache
    @CacheEvict(value = "packages", allEntries = true)
    public PackageDTO updatePackage(Integer packageId, PackageDTO packageDTO) {
        Optional<TravelPackage> packageOpt = packageRepository.findById(packageId);
        if (packageOpt.isPresent()) {
            TravelPackage packageEntity = packageOpt.get();
            updateEntityFromDTO(packageEntity, packageDTO);
            TravelPackage savedPackage = packageRepository.save(packageEntity);
            return convertToDTO(savedPackage);
        }
        return null;
    }

    // Delete package (soft delete) - clear cache
    @CacheEvict(value = "packages", allEntries = true)
    public boolean deletePackage(Integer packageId) {
        Optional<TravelPackage> packageOpt = packageRepository.findById(packageId);
        if (packageOpt.isPresent()) {
            TravelPackage packageEntity = packageOpt.get();
            packageEntity.setIsActive(false);
            packageRepository.save(packageEntity);
            return true;
        }
        return false;
    }

    // Convert Entity to DTO
    private PackageDTO convertToDTO(TravelPackage packageEntity) {
        PackageDTO dto = new PackageDTO();
        dto.setPackageId(packageEntity.getPackageId());
        dto.setPackageName(packageEntity.getPackageName());
        dto.setPackageCategory(packageEntity.getPackageCategory());
        dto.setPackageCategoryId(packageEntity.getPackageCategoryId());
        dto.setPackageDescription(packageEntity.getPackageDescription());
        dto.setPackageDuration(packageEntity.getPackageDuration());
        dto.setPackagePrice(packageEntity.getPackagePrice());
        dto.setOriginalPrice(packageEntity.getOriginalPrice());
        dto.setDiscountPercentage(packageEntity.getDiscountPercentage());
        dto.setPackageImageUrl(packageEntity.getPackageImageUrl());
        
        // Convert JSON strings to Lists (you might want to use a JSON library like Jackson)
        // For now, we'll handle this in the controller or use a proper JSON converter
        dto.setPackageFeatures(parseJsonToList(packageEntity.getPackageFeatures()));
        dto.setPackageHighlights(parseJsonToList(packageEntity.getPackageHighlights()));
        
        dto.setRating(packageEntity.getRating());
        dto.setReviewsCount(packageEntity.getReviewsCount());
        dto.setIsActive(packageEntity.getIsActive());
        dto.setIsFeatured(packageEntity.getIsFeatured());
        dto.setSortOrder(packageEntity.getSortOrder());
        dto.setCreatedAt(packageEntity.getCreatedAt());
        dto.setUpdatedAt(packageEntity.getUpdatedAt());
        dto.setVehicleId(packageEntity.getVehicleId());
        dto.setMaxPassengers(packageEntity.getMaxPassengers());
        
        // Fetch vehicle name if vehicleId exists
        if (packageEntity.getVehicleId() != null) {
            Optional<Vehicle> vehicleOpt = vehicleRepository.findById(packageEntity.getVehicleId().intValue());
            if (vehicleOpt.isPresent()) {
                dto.setVehicleName(vehicleOpt.get().getName());
            }
        }
        
        return dto;
    }

    // Convert DTO to Entity
    private TravelPackage convertToEntity(PackageDTO packageDTO) {
        TravelPackage packageEntity = new TravelPackage();
        packageEntity.setPackageName(packageDTO.getPackageName());
        packageEntity.setPackageCategory(packageDTO.getPackageCategory());
        packageEntity.setPackageCategoryId(packageDTO.getPackageCategoryId());
        packageEntity.setPackageDescription(packageDTO.getPackageDescription());
        packageEntity.setPackageDuration(packageDTO.getPackageDuration());
        packageEntity.setPackagePrice(packageDTO.getPackagePrice());
        packageEntity.setOriginalPrice(packageDTO.getOriginalPrice());
        packageEntity.setDiscountPercentage(packageDTO.getDiscountPercentage());
        packageEntity.setPackageImageUrl(packageDTO.getPackageImageUrl());
        
        // Convert Lists to JSON strings
        packageEntity.setPackageFeatures(convertListToJson(packageDTO.getPackageFeatures()));
        packageEntity.setPackageHighlights(convertListToJson(packageDTO.getPackageHighlights()));
        
        packageEntity.setRating(packageDTO.getRating());
        packageEntity.setReviewsCount(packageDTO.getReviewsCount());
        packageEntity.setIsActive(packageDTO.getIsActive() != null ? packageDTO.getIsActive() : true);
        packageEntity.setIsFeatured(packageDTO.getIsFeatured() != null ? packageDTO.getIsFeatured() : false);
        packageEntity.setSortOrder(packageDTO.getSortOrder());
        packageEntity.setVehicleId(packageDTO.getVehicleId());
        packageEntity.setMaxPassengers(packageDTO.getMaxPassengers());
        
        return packageEntity;
    }

    // Update entity from DTO
    private void updateEntityFromDTO(TravelPackage packageEntity, PackageDTO packageDTO) {
        if (packageDTO.getPackageName() != null) {
            packageEntity.setPackageName(packageDTO.getPackageName());
        }
        if (packageDTO.getPackageCategory() != null) {
            packageEntity.setPackageCategory(packageDTO.getPackageCategory());
        }
        if (packageDTO.getPackageCategoryId() != null) {
            packageEntity.setPackageCategoryId(packageDTO.getPackageCategoryId());
        }
        if (packageDTO.getPackageDescription() != null) {
            packageEntity.setPackageDescription(packageDTO.getPackageDescription());
        }
        if (packageDTO.getPackageDuration() != null) {
            packageEntity.setPackageDuration(packageDTO.getPackageDuration());
        }
        if (packageDTO.getPackagePrice() != null) {
            packageEntity.setPackagePrice(packageDTO.getPackagePrice());
        }
        if (packageDTO.getOriginalPrice() != null) {
            packageEntity.setOriginalPrice(packageDTO.getOriginalPrice());
        }
        if (packageDTO.getDiscountPercentage() != null) {
            packageEntity.setDiscountPercentage(packageDTO.getDiscountPercentage());
        }
        if (packageDTO.getPackageImageUrl() != null) {
            packageEntity.setPackageImageUrl(packageDTO.getPackageImageUrl());
        }
        if (packageDTO.getPackageFeatures() != null) {
            packageEntity.setPackageFeatures(convertListToJson(packageDTO.getPackageFeatures()));
        }
        if (packageDTO.getPackageHighlights() != null) {
            packageEntity.setPackageHighlights(convertListToJson(packageDTO.getPackageHighlights()));
        }
        if (packageDTO.getRating() != null) {
            packageEntity.setRating(packageDTO.getRating());
        }
        if (packageDTO.getReviewsCount() != null) {
            packageEntity.setReviewsCount(packageDTO.getReviewsCount());
        }
        if (packageDTO.getIsActive() != null) {
            packageEntity.setIsActive(packageDTO.getIsActive());
        }
        if (packageDTO.getIsFeatured() != null) {
            packageEntity.setIsFeatured(packageDTO.getIsFeatured());
        }
        if (packageDTO.getSortOrder() != null) {
            packageEntity.setSortOrder(packageDTO.getSortOrder());
        }
    }

    // Helper methods for JSON conversion (simplified - you might want to use Jackson)
    private List<String> parseJsonToList(String jsonString) {
        // Simple JSON array parsing - you might want to use a proper JSON library
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return List.of();
        }
        // Remove brackets and split by comma
        String cleanJson = jsonString.replaceAll("[\\[\\]\"]", "").trim();
        if (cleanJson.isEmpty()) {
            return List.of();
        }
        return List.of(cleanJson.split(","));
    }

    private String convertListToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        return "[\"" + String.join("\",\"", list) + "\"]";
    }
}
