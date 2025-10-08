package com.sairajtravels.site.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "VehicleCharges")
public class VehicleCharges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ChargeId")
    private Integer chargeId;

    // Foreign Key reference to Vehicle
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VehicleId")
    private Vehicle vehicle;

    // Only the columns that actually exist in the database
    @Column(name = "DriverAllowance", precision = 10, scale = 2)
    private BigDecimal driverAllowance;

    @Column(name = "NightCharge", precision = 10, scale = 2)
    private BigDecimal nightCharge;

    @Column(name = "FuelIncluded")
    private Boolean fuelIncluded;

    @Column(name = "TollIncluded")
    private Boolean tollIncluded;

    @Column(name = "ParkingIncluded")
    private Boolean parkingIncluded;

    // Fields that don't exist in database but are used in code - make them transient
    @Transient
    private String chargeType;

    @Transient
    private BigDecimal chargeAmount;

    @Transient
    private Boolean isMandatory;

    @Transient
    private String description;

    @Transient
    private java.time.LocalDateTime createdAt;

    // ✅ Constructors
    public VehicleCharges() {}

    public VehicleCharges(Vehicle vehicle, BigDecimal driverAllowance, BigDecimal nightCharge,
                          Boolean fuelIncluded, Boolean tollIncluded, Boolean parkingIncluded) {
        this.vehicle = vehicle;
        this.driverAllowance = driverAllowance;
        this.nightCharge = nightCharge;
        this.fuelIncluded = fuelIncluded;
        this.tollIncluded = tollIncluded;
        this.parkingIncluded = parkingIncluded;
    }

    // ✅ Getters & Setters
    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public BigDecimal getDriverAllowance() {
        return driverAllowance;
    }

    public void setDriverAllowance(BigDecimal driverAllowance) {
        this.driverAllowance = driverAllowance;
    }

    public BigDecimal getNightCharge() {
        return nightCharge;
    }

    public void setNightCharge(BigDecimal nightCharge) {
        this.nightCharge = nightCharge;
    }

    public Boolean getFuelIncluded() {
        return fuelIncluded;
    }

    public void setFuelIncluded(Boolean fuelIncluded) {
        this.fuelIncluded = fuelIncluded;
    }

    public Boolean getTollIncluded() {
        return tollIncluded;
    }

    public void setTollIncluded(Boolean tollIncluded) {
        this.tollIncluded = tollIncluded;
    }

    public Boolean getParkingIncluded() {
        return parkingIncluded;
    }

    public void setParkingIncluded(Boolean parkingIncluded) {
        this.parkingIncluded = parkingIncluded;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
