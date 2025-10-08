package com.sairajtravels.site.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_id")
    private Integer busId;

    @Column(name = "bus_name", nullable = false)
    private String name;

    @Column(name = "bus_capacity", nullable = false)
    private Integer capacity;

    @Column(name = "bus_features")
    private String features;

    @Column(name = "bus_image_url")
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters & Setters
    public Integer getBusId() { return busId; }
    public void setBusId(Integer busId) { this.busId = busId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getFeatures() { return features; }
    public void setFeatures(String features) { this.features = features; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
