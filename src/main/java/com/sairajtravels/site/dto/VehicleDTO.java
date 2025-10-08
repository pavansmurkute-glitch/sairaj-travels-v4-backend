package com.sairajtravels.site.dto;

import java.time.LocalDateTime;

public class VehicleDTO {
    private Integer vehicleId;
    private String name;
    private String type;
    private Integer capacity;
    private Boolean isAC;
    private String description;
    private LocalDateTime createdAt;
    private String mainImageUrl;

    // ✅ Default constructor (Spring/DTO needs this)
    public VehicleDTO() {}

    // ✅ Getters & Setters
    public Integer getVehicleId() {
        return vehicleId;
    }
    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean getIsAC() {
        return isAC;
    }
    public void setIsAC(Boolean isAC) {
        this.isAC = isAC;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }
    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }
}
