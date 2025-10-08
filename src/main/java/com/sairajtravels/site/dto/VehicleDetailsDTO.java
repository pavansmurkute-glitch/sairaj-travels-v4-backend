package com.sairajtravels.site.dto;

import java.math.BigDecimal;
import java.util.List;

public class VehicleDetailsDTO {

    private VehicleDTO vehicle;
    private List<VehiclePricingDTO> pricing;
    private List<VehicleChargesDTO> charges;
    private List<VehicleTermDTO> terms;
    private List<VehicleImageDTO> images;

    // --- getters & setters ---
    public VehicleDTO getVehicle() { return vehicle; }
    public void setVehicle(VehicleDTO vehicle) { this.vehicle = vehicle; }

    public List<VehiclePricingDTO> getPricing() { return pricing; }
    public void setPricing(List<VehiclePricingDTO> pricing) { this.pricing = pricing; }

    public List<VehicleChargesDTO> getCharges() { return charges; }
    public void setCharges(List<VehicleChargesDTO> charges) { this.charges = charges; }

    public List<VehicleTermDTO> getTerms() { return terms; }
    public void setTerms(List<VehicleTermDTO> terms) { this.terms = terms; }

    public List<VehicleImageDTO> getImages() { return images; }
    public void setImages(List<VehicleImageDTO> images) { this.images = images; }

    // --- Nested DTOs ---
    public static class VehicleDTO {
        private Integer vehicleId;
        private String name;
        private String type;
        private Integer capacity;
        private Boolean isAC;
        private String description;
        private String mainImageUrl; // ✅ NEW FIELD

        public Integer getVehicleId() { return vehicleId; }
        public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Integer getCapacity() { return capacity; }
        public void setCapacity(Integer capacity) { this.capacity = capacity; }
        public Boolean getIsAC() { return isAC; }
        public void setIsAC(Boolean isAC) { this.isAC = isAC; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getMainImageUrl() { return mainImageUrl; }
        public void setMainImageUrl(String mainImageUrl) { this.mainImageUrl = mainImageUrl; }
    }

    public static class VehiclePricingDTO {
        private Integer pricingId;
        private BigDecimal ratePerKm;
        private BigDecimal extraKmRate;
        private BigDecimal extraHourRate;
        private Integer minKmPerDay;
        private BigDecimal packageRate;
        private Integer packageKm;
        private Integer packageHours;
        private String rateType;

        public Integer getPricingId() { return pricingId; }
        public void setPricingId(Integer pricingId) { this.pricingId = pricingId; }
        public BigDecimal getRatePerKm() { return ratePerKm; }
        public void setRatePerKm(BigDecimal ratePerKm) { this.ratePerKm = ratePerKm; }
        public BigDecimal getExtraKmRate() { return extraKmRate; }
        public void setExtraKmRate(BigDecimal extraKmRate) { this.extraKmRate = extraKmRate; }
        public BigDecimal getExtraHourRate() { return extraHourRate; }
        public void setExtraHourRate(BigDecimal extraHourRate) { this.extraHourRate = extraHourRate; }
        public Integer getMinKmPerDay() { return minKmPerDay; }
        public void setMinKmPerDay(Integer minKmPerDay) { this.minKmPerDay = minKmPerDay; }
        public BigDecimal getPackageRate() { return packageRate; }
        public void setPackageRate(BigDecimal packageRate) { this.packageRate = packageRate; }
        public Integer getPackageKm() { return packageKm; }
        public void setPackageKm(Integer packageKm) { this.packageKm = packageKm; }
        public Integer getPackageHours() { return packageHours; }
        public void setPackageHours(Integer packageHours) { this.packageHours = packageHours; }
        public String getRateType() { return rateType; }
        public void setRateType(String rateType) { this.rateType = rateType; }
    }

    public static class VehicleChargesDTO {
        private Integer chargeId;
        private BigDecimal driverAllowance;
        private Boolean tollIncluded;
        private Boolean parkingIncluded;
        private Boolean fuelIncluded;
        private BigDecimal nightCharge;

        public Integer getChargeId() { return chargeId; }
        public void setChargeId(Integer chargeId) { this.chargeId = chargeId; }
        public BigDecimal getDriverAllowance() { return driverAllowance; }
        public void setDriverAllowance(BigDecimal driverAllowance) { this.driverAllowance = driverAllowance; }
        public Boolean getTollIncluded() { return tollIncluded; }
        public void setTollIncluded(Boolean tollIncluded) { this.tollIncluded = tollIncluded; }
        public Boolean getParkingIncluded() { return parkingIncluded; }
        public void setParkingIncluded(Boolean parkingIncluded) { this.parkingIncluded = parkingIncluded; }
        public Boolean getFuelIncluded() { return fuelIncluded; }
        public void setFuelIncluded(Boolean fuelIncluded) { this.fuelIncluded = fuelIncluded; }
        public BigDecimal getNightCharge() { return nightCharge; }
        public void setNightCharge(BigDecimal nightCharge) { this.nightCharge = nightCharge; }
    }

    public static class VehicleTermDTO {
        private Integer termId;
        private String termText;

        public Integer getTermId() { return termId; }
        public void setTermId(Integer termId) { this.termId = termId; }
        public String getTermText() { return termText; }
        public void setTermText(String termText) { this.termText = termText; }
    }

    public static class VehicleImageDTO {
        private Integer imageId;
        private String imageUrl;

        public Integer getImageId() { return imageId; }
        public void setImageId(Integer imageId) { this.imageId = imageId; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }
}
