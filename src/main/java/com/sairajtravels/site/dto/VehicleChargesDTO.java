package com.sairajtravels.site.dto;

import java.math.BigDecimal;

public class VehicleChargesDTO {
    private Integer chargeId;
    private Integer vehicleId;
    private BigDecimal driverAllowance;
    private BigDecimal nightCharge;
    private Boolean fuelIncluded;
    private Boolean tollIncluded;
    private Boolean parkingIncluded;

    public VehicleChargesDTO() {}

    public VehicleChargesDTO(Integer chargeId, Integer vehicleId, BigDecimal driverAllowance,
                             BigDecimal nightCharge, Boolean fuelIncluded, Boolean tollIncluded,
                             Boolean parkingIncluded) {
        this.chargeId = chargeId;
        this.vehicleId = vehicleId;
        this.driverAllowance = driverAllowance;
        this.nightCharge = nightCharge;
        this.fuelIncluded = fuelIncluded;
        this.tollIncluded = tollIncluded;
        this.parkingIncluded = parkingIncluded;
    }

    // ✅ Getters & Setters
    public Integer getChargeId() { return chargeId; }
    public void setChargeId(Integer chargeId) { this.chargeId = chargeId; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public BigDecimal getDriverAllowance() { return driverAllowance; }
    public void setDriverAllowance(BigDecimal driverAllowance) { this.driverAllowance = driverAllowance; }

    public BigDecimal getNightCharge() { return nightCharge; }
    public void setNightCharge(BigDecimal nightCharge) { this.nightCharge = nightCharge; }

    public Boolean getFuelIncluded() { return fuelIncluded; }
    public void setFuelIncluded(Boolean fuelIncluded) { this.fuelIncluded = fuelIncluded; }

    public Boolean getTollIncluded() { return tollIncluded; }
    public void setTollIncluded(Boolean tollIncluded) { this.tollIncluded = tollIncluded; }

    public Boolean getParkingIncluded() { return parkingIncluded; }
    public void setParkingIncluded(Boolean parkingIncluded) { this.parkingIncluded = parkingIncluded; }
}
