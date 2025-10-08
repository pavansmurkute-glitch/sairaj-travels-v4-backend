package com.sairajtravels.site.dto;

public class VehicleImageDTO {
    private Integer imageId;
    private Integer vehicleId;
    private String imageUrl;

    public VehicleImageDTO() {}

    public VehicleImageDTO(Integer imageId, Integer vehicleId, String imageUrl) {
        this.imageId = imageId;
        this.vehicleId = vehicleId;
        this.imageUrl = imageUrl;
    }

    public Integer getImageId() { return imageId; }
    public void setImageId(Integer imageId) { this.imageId = imageId; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
