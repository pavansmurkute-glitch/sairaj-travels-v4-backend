package com.sairajtravels.site.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PackageDTO {
    private Integer packageId;
    private String packageName;
    private String packageCategory;
    private String packageCategoryId;
    private String packageDescription;
    private String packageDuration;
    private BigDecimal packagePrice;
    private BigDecimal originalPrice;
    private BigDecimal discountPercentage;
    private String packageImageUrl;
    private List<String> packageFeatures;
    private List<String> packageHighlights;
    private BigDecimal rating;
    private Integer reviewsCount;
    private Boolean isActive;
    private Boolean isFeatured;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long vehicleId;
    private String vehicleName; // For display purposes
    private Integer maxPassengers;

    // Constructors
    public PackageDTO() {}

    public PackageDTO(String packageName, String packageCategory, String packageCategoryId, 
                      String packageDescription, String packageDuration, BigDecimal packagePrice) {
        this.packageName = packageName;
        this.packageCategory = packageCategory;
        this.packageCategoryId = packageCategoryId;
        this.packageDescription = packageDescription;
        this.packageDuration = packageDuration;
        this.packagePrice = packagePrice;
    }

    // Getters & Setters
    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageCategory() {
        return packageCategory;
    }

    public void setPackageCategory(String packageCategory) {
        this.packageCategory = packageCategory;
    }

    public String getPackageCategoryId() {
        return packageCategoryId;
    }

    public void setPackageCategoryId(String packageCategoryId) {
        this.packageCategoryId = packageCategoryId;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public String getPackageDuration() {
        return packageDuration;
    }

    public void setPackageDuration(String packageDuration) {
        this.packageDuration = packageDuration;
    }

    public BigDecimal getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(BigDecimal packagePrice) {
        this.packagePrice = packagePrice;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getPackageImageUrl() {
        return packageImageUrl;
    }

    public void setPackageImageUrl(String packageImageUrl) {
        this.packageImageUrl = packageImageUrl;
    }

    public List<String> getPackageFeatures() {
        return packageFeatures;
    }

    public void setPackageFeatures(List<String> packageFeatures) {
        this.packageFeatures = packageFeatures;
    }

    public List<String> getPackageHighlights() {
        return packageHighlights;
    }

    public void setPackageHighlights(List<String> packageHighlights) {
        this.packageHighlights = packageHighlights;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public Integer getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(Integer maxPassengers) {
        this.maxPassengers = maxPassengers;
    }
}
