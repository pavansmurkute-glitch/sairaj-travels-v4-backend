package com.sairajtravels.site.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "packages")
public class TravelPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Integer packageId;

    @Column(name = "package_name", nullable = false, length = 200)
    private String packageName;

    @Column(name = "package_category", nullable = false, length = 50)
    private String packageCategory;

    @Column(name = "package_category_id", nullable = false, length = 50)
    private String packageCategoryId;

    @Column(name = "package_description", columnDefinition = "TEXT")
    private String packageDescription;

    @Column(name = "package_duration", length = 100)
    private String packageDuration;

    @Column(name = "package_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal packagePrice;

    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "package_image_url", length = 500)
    private String packageImageUrl;

    @Column(name = "package_features", columnDefinition = "TEXT")
    private String packageFeatures; // JSON string of features array

    @Column(name = "package_highlights", columnDefinition = "TEXT")
    private String packageHighlights; // JSON string of highlights array

    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(name = "reviews_count")
    private Integer reviewsCount;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "max_passengers")
    private Integer maxPassengers;

    // Constructors
    public TravelPackage() {}

    public TravelPackage(String packageName, String packageCategory, String packageCategoryId, 
                   String packageDescription, String packageDuration, BigDecimal packagePrice) {
        this.packageName = packageName;
        this.packageCategory = packageCategory;
        this.packageCategoryId = packageCategoryId;
        this.packageDescription = packageDescription;
        this.packageDuration = packageDuration;
        this.packagePrice = packagePrice;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public String getPackageFeatures() {
        return packageFeatures;
    }

    public void setPackageFeatures(String packageFeatures) {
        this.packageFeatures = packageFeatures;
    }

    public String getPackageHighlights() {
        return packageHighlights;
    }

    public void setPackageHighlights(String packageHighlights) {
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

    public Integer getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(Integer maxPassengers) {
        this.maxPassengers = maxPassengers;
    }
}
