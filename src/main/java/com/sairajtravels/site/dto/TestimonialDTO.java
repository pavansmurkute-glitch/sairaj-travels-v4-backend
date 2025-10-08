package com.sairajtravels.site.dto;

import java.time.LocalDateTime;

public class TestimonialDTO {
    
    private Long id;
    private String customerName;
    private String customerType;
    private String testimonialText;
    private Integer rating;
    private String avatarLetter;
    private Boolean isActive;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public TestimonialDTO() {}
    
    public TestimonialDTO(Long id, String customerName, String customerType, String testimonialText, 
                         Integer rating, String avatarLetter, Boolean isActive, Integer sortOrder) {
        this.id = id;
        this.customerName = customerName;
        this.customerType = customerType;
        this.testimonialText = testimonialText;
        this.rating = rating;
        this.avatarLetter = avatarLetter;
        this.isActive = isActive;
        this.sortOrder = sortOrder;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerType() {
        return customerType;
    }
    
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
    
    public String getTestimonialText() {
        return testimonialText;
    }
    
    public void setTestimonialText(String testimonialText) {
        this.testimonialText = testimonialText;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getAvatarLetter() {
        return avatarLetter;
    }
    
    public void setAvatarLetter(String avatarLetter) {
        this.avatarLetter = avatarLetter;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
}
