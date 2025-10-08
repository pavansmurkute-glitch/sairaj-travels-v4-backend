package com.sairajtravels.site.dto;

public class VehicleTermDTO {
    private Integer termId;
    private Integer vehicleId;
    private String termText;

    public VehicleTermDTO() {}

    public VehicleTermDTO(Integer termId, Integer vehicleId, String termText) {
        this.termId = termId;
        this.vehicleId = vehicleId;
        this.termText = termText;
    }

    // ✅ Getters & Setters
    public Integer getTermId() { return termId; }
    public void setTermId(Integer termId) { this.termId = termId; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public String getTermText() { return termText; }
    public void setTermText(String termText) { this.termText = termText; }
}
