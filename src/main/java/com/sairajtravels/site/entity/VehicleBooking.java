package com.sairajtravels.site.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "VehicleBookings")
public class VehicleBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingId")
    private Integer bookingId;

    // Foreign Key reference to Vehicle
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VehicleId")
    private Vehicle vehicle;

    // Foreign Key reference to Package (optional)
    @Column(name = "PackageId")
    private Integer packageId;

    @Column(name = "CustomerName", length = 100)
    private String customerName;

    @Column(name = "CustomerPhone", length = 20)
    private String customerPhone;

    @Column(name = "CustomerEmail", length = 100)
    private String customerEmail;

    @Column(name = "PickupLocation", length = 200)
    private String pickupLocation;

    @Column(name = "DropLocation", length = 200)
    private String dropLocation;

    @Column(name = "TripDate", nullable = false)
    private LocalDate tripDate;

    @Column(name = "ReturnDate")
    private LocalDate returnDate;

    @Column(name = "TripTime", length = 20)
    private String tripTime;

    @Column(name = "Passengers")
    private Integer passengers;

    @Column(name = "Luggage", length = 100)
    private String luggage;

    @Column(name = "SpecialRequests", length = 500)
    private String specialRequests;

    @Column(name = "Status", length = 20)
    private String status;

    @Column(name = "RequestedAt")
    private LocalDateTime requestedAt;

    // ✅ Constructors
    public VehicleBooking() {}

    public VehicleBooking(Vehicle vehicle, String customerName, String customerPhone,
                          LocalDate tripDate, String status, LocalDateTime requestedAt) {
        this.vehicle = vehicle;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.tripDate = tripDate;
        this.status = status;
        this.requestedAt = requestedAt;
    }

    // ✅ Getters & Setters
    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public LocalDate getTripDate() {
        return tripDate;
    }

    public void setTripDate(LocalDate tripDate) {
        this.tripDate = tripDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

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
