package com.sairajtravels.site.dto;

import java.math.BigDecimal;

public class VehiclePricingDTO {
    private Integer pricingId;
    private Integer vehicleId;
    private String rateType;
    private BigDecimal ratePerKm;
    private Integer minKmPerDay;
    private Integer packageHours;
    private Integer packageKm;
    private BigDecimal packageRate;
    private BigDecimal extraKmRate;
    private BigDecimal extraHourRate;

    public VehiclePricingDTO() {}

    public VehiclePricingDTO(Integer pricingId, Integer vehicleId, String rateType,
                             BigDecimal ratePerKm, Integer minKmPerDay, Integer packageHours,
                             Integer packageKm, BigDecimal packageRate, BigDecimal extraKmRate,
                             BigDecimal extraHourRate) {
        this.pricingId = pricingId;
        this.vehicleId = vehicleId;
        this.rateType = rateType;
        this.ratePerKm = ratePerKm;
        this.minKmPerDay = minKmPerDay;
        this.packageHours = packageHours;
        this.packageKm = packageKm;
        this.packageRate = packageRate;
        this.extraKmRate = extraKmRate;
        this.extraHourRate = extraHourRate;
    }

    // ✅ Getters & Setters
    public Integer getPricingId() { return pricingId; }
    public void setPricingId(Integer pricingId) { this.pricingId = pricingId; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public String getRateType() { return rateType; }
    public void setRateType(String rateType) { this.rateType = rateType; }

    public BigDecimal getRatePerKm() { return ratePerKm; }
    public void setRatePerKm(BigDecimal ratePerKm) { this.ratePerKm = ratePerKm; }

    public Integer getMinKmPerDay() { return minKmPerDay; }
    public void setMinKmPerDay(Integer minKmPerDay) { this.minKmPerDay = minKmPerDay; }

    public Integer getPackageHours() { return packageHours; }
    public void setPackageHours(Integer packageHours) { this.packageHours = packageHours; }

    public Integer getPackageKm() { return packageKm; }
    public void setPackageKm(Integer packageKm) { this.packageKm = packageKm; }

    public BigDecimal getPackageRate() { return packageRate; }
    public void setPackageRate(BigDecimal packageRate) { this.packageRate = packageRate; }

    public BigDecimal getExtraKmRate() { return extraKmRate; }
    public void setExtraKmRate(BigDecimal extraKmRate) { this.extraKmRate = extraKmRate; }

    public BigDecimal getExtraHourRate() { return extraHourRate; }
    public void setExtraHourRate(BigDecimal extraHourRate) { this.extraHourRate = extraHourRate; }
}
