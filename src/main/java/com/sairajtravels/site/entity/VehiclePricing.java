package com.sairajtravels.site.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "VehiclePricing")
public class VehiclePricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PricingId")
    private Integer pricingId;

    // Foreign Key reference to Vehicle
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VehicleId")
    private Vehicle vehicle;

    // Only the columns that actually exist in the database
    @Column(name = "RateType", length = 20)
    private String rateType;

    @Column(name = "RatePerKm", precision = 10, scale = 2)
    private BigDecimal ratePerKm;

    @Column(name = "MinKmPerDay")
    private Integer minKmPerDay;

    @Column(name = "PackageHours")
    private Integer packageHours;

    @Column(name = "PackageKm")
    private Integer packageKm;

    @Column(name = "PackageRate", precision = 10, scale = 2)
    private BigDecimal packageRate;

    @Column(name = "ExtraKmRate", precision = 10, scale = 2)
    private BigDecimal extraKmRate;

    @Column(name = "ExtraHourRate", precision = 10, scale = 2)
    private BigDecimal extraHourRate;

    // Fields that don't exist in database but are used in code - make them transient
    @Transient
    private BigDecimal basePrice;

    @Transient
    private BigDecimal perHourRate;

    @Transient
    private BigDecimal nightCharge;

    @Transient
    private BigDecimal tollCharges;

    @Transient
    private BigDecimal parkingCharges;

    @Transient
    private BigDecimal mealCharges;

    @Transient
    private BigDecimal emergencyCharges;

    @Transient
    private Boolean isActive;

    @Transient
    private java.time.LocalDateTime createdAt;

    @Transient
    private java.time.LocalDateTime updatedAt;

    // ✅ Constructors
    public VehiclePricing() {}

    public VehiclePricing(Vehicle vehicle, BigDecimal basePrice, BigDecimal ratePerKm,
                          BigDecimal perHourRate, BigDecimal nightCharge, BigDecimal tollCharges,
                          BigDecimal parkingCharges, BigDecimal mealCharges, BigDecimal emergencyCharges) {
        this.vehicle = vehicle;
        this.basePrice = basePrice;
        this.ratePerKm = ratePerKm;
        this.perHourRate = perHourRate;
        this.nightCharge = nightCharge;
        this.tollCharges = tollCharges;
        this.parkingCharges = parkingCharges;
        this.mealCharges = mealCharges;
        this.emergencyCharges = emergencyCharges;
        this.isActive = true;
    }

    // ✅ Getters & Setters
    public Integer getPricingId() {
        return pricingId;
    }

    public void setPricingId(Integer pricingId) {
        this.pricingId = pricingId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getPerKmRate() {
        return ratePerKm;
    }

    public void setPerKmRate(BigDecimal ratePerKm) {
        this.ratePerKm = ratePerKm;
    }

    public BigDecimal getPerHourRate() {
        return perHourRate;
    }

    public void setPerHourRate(BigDecimal perHourRate) {
        this.perHourRate = perHourRate;
    }

    public BigDecimal getNightCharge() {
        return nightCharge;
    }

    public void setNightCharge(BigDecimal nightCharge) {
        this.nightCharge = nightCharge;
    }

    public BigDecimal getTollCharges() {
        return tollCharges;
    }

    public void setTollCharges(BigDecimal tollCharges) {
        this.tollCharges = tollCharges;
    }

    public BigDecimal getParkingCharges() {
        return parkingCharges;
    }

    public void setParkingCharges(BigDecimal parkingCharges) {
        this.parkingCharges = parkingCharges;
    }

    public BigDecimal getMealCharges() {
        return mealCharges;
    }

    public void setMealCharges(BigDecimal mealCharges) {
        this.mealCharges = mealCharges;
    }

    public BigDecimal getEmergencyCharges() {
        return emergencyCharges;
    }

    public void setEmergencyCharges(BigDecimal emergencyCharges) {
        this.emergencyCharges = emergencyCharges;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public java.time.LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Additional methods that are referenced in the code
    public String getRateType() {
        return rateType != null ? rateType : "PER_KM"; // Return actual field or default
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getRatePerKm() {
        return ratePerKm;
    }

    public void setRatePerKm(BigDecimal ratePerKm) {
        this.ratePerKm = ratePerKm;
    }

    public Integer getMinKmPerDay() {
        return minKmPerDay;
    }

    public void setMinKmPerDay(Integer minKmPerDay) {
        this.minKmPerDay = minKmPerDay;
    }

    public Integer getPackageHours() {
        return packageHours;
    }

    public void setPackageHours(Integer packageHours) {
        this.packageHours = packageHours;
    }

    public Integer getPackageKm() {
        return packageKm;
    }

    public void setPackageKm(Integer packageKm) {
        this.packageKm = packageKm;
    }

    public BigDecimal getPackageRate() {
        return packageRate;
    }

    public void setPackageRate(BigDecimal packageRate) {
        this.packageRate = packageRate;
    }

    public BigDecimal getExtraKmRate() {
        return extraKmRate;
    }

    public void setExtraKmRate(BigDecimal extraKmRate) {
        this.extraKmRate = extraKmRate;
    }

    public BigDecimal getExtraHourRate() {
        return extraHourRate;
    }

    public void setExtraHourRate(BigDecimal extraHourRate) {
        this.extraHourRate = extraHourRate;
    }
}
