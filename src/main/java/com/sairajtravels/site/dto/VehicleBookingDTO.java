package com.sairajtravels.site.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class VehicleBookingDTO {
    private Integer bookingId;
    private Integer vehicleId;
    @JsonProperty("packageId")
    private Integer packageId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String pickupLocation;
    private String dropLocation;
    private LocalDate tripDate;
    private LocalDate returnDate;
    private String tripTime;
    private Integer passengers;
    private String luggage;
    private String specialRequests;
    private String status;
    private LocalDateTime requestedAt;

    public VehicleBookingDTO() {}

    public VehicleBookingDTO(Integer bookingId, Integer vehicleId, String customerName,
                             String customerPhone, LocalDate tripDate, String status,
                             LocalDateTime requestedAt) {
        this.bookingId = bookingId;
        this.vehicleId = vehicleId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.tripDate = tripDate;
        this.status = status;
        this.requestedAt = requestedAt;
    }

    // ✅ Getters & Setters
    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public Integer getPackageId() { return packageId; }
    public void setPackageId(Integer packageId) { this.packageId = packageId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public LocalDate getTripDate() { return tripDate; }
    public void setTripDate(LocalDate tripDate) { this.tripDate = tripDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropLocation() { return dropLocation; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public String getTripTime() { return tripTime; }
    public void setTripTime(String tripTime) { this.tripTime = tripTime; }

    public Integer getPassengers() { return passengers; }
    public void setPassengers(Integer passengers) { this.passengers = passengers; }

    public String getLuggage() { return luggage; }
    public void setLuggage(String luggage) { this.luggage = luggage; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
}
