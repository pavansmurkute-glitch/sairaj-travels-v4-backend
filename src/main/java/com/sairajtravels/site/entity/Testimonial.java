package com.sairajtravels.site.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "testimonials")
public class Testimonial {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;
    
    @Column(name = "customer_type", nullable = false, length = 50)
    private String customerType;
    
    @Column(name = "testimonial_text", nullable = false, columnDefinition = "TEXT")
    private String testimonialText;
    
    @Column(name = "rating", nullable = false)
    private Integer rating = 5;
    
    @Column(name = "avatar_letter", nullable = false, length = 1)
    private String avatarLetter;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public Testimonial() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Testimonial(String customerName, String customerType, String testimonialText, 
                      Integer rating, String avatarLetter, Boolean isActive, Integer sortOrder) {
        this();
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
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
